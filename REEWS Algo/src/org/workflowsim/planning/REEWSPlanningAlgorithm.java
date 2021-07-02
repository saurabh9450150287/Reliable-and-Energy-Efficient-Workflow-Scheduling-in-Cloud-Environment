/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.workflowsim.planning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.cloudbus.cloudsim.Consts;
import org.workflowsim.CondorVM;
import org.workflowsim.FileItem;
import org.workflowsim.Task;
import org.workflowsim.utils.Parameters;

/**
 *
 * @author Saurabh
 */


public class REEWSPlanningAlgorithm extends BasePlanningAlgorithm
{
    
    private Map<Task, Map<CondorVM, Double>> computationCosts;
    private Map<Task, Map<Task, Double>> transferCosts;
    private Map<Task, Double> rank;
    private Map<Task, Double> earliestFinishTimes;
    private Map<Task, Double> earliestStartTimes;
    private double averageBandwidth;
    private double deadline;
    private ArrayList<ArrayList<Task>> clusters;
    private Map<Task,Double> subTargetTime;
    private Map<Task,Boolean> allocate;
    private Map<Task,Double> lft;
    private Map<Task,Double> fet;
    private double fre[];
    private Map<CondorVM,Double> freq;
    public REEWSPlanningAlgorithm() throws NullPointerException{
        System.out.println("CONSTRUCTOR CALLED");
        System.out.println("DONE");
        computationCosts = new HashMap<>();
        
        transferCosts = new HashMap<>();
        rank = new HashMap<>();
        
        earliestFinishTimes = new HashMap<>();
        earliestStartTimes = new HashMap<>();
        clusters = new ArrayList<>();
        deadline =1000;
        subTargetTime = new HashMap<>();
        
        allocate = new HashMap<>();
        
        lft = new HashMap<>();
        freq = new HashMap<>();
        fet = new HashMap<>();
        fre = new double[4];
        fre[0]=2.8;
        fre[1]=fre[0]*.7;
        fre[2]=fre[0]*.5;
        fre[3]=fre[0]*.4;
        System.out.println("CONSTRUCTOR ENDeD");
    }
    private void calculateComputationCosts() {
        for (Task task : getTaskList()) {
            Map<CondorVM, Double> costsVm = new HashMap<>();
            for (Object vmObject : getVmList()) {
                CondorVM vm = (CondorVM) vmObject;
                if (vm.getNumberOfPes() < task.getNumberOfPes()) {
                    costsVm.put(vm, Double.MAX_VALUE);
                } else {
                    costsVm.put(vm,
                            task.getCloudletTotalLength() / vm.getMips());
                }
            }
            computationCosts.put(task, costsVm);
        }
    }
    private double calculateAverageBandwidth() {
        double avg = 0.0;
        for (Object vmObject : getVmList()) {
            CondorVM vm = (CondorVM) vmObject;
            avg += vm.getBw();
        }
        return avg / getVmList().size();
    }
    private void calculateTransferCosts() {
        // Initializing the matrix
        for (Task task1 : getTaskList()) {
            Map<Task, Double> taskTransferCosts = new HashMap<>();
            for (Task task2 : getTaskList()) {
                taskTransferCosts.put(task2, 0.0);
            }
            transferCosts.put(task1, taskTransferCosts);
        }

        // Calculating the actual values
        for (Task parent : getTaskList()) {
            for (Task child : parent.getChildList()) {
                transferCosts.get(parent).put(child,
                        calculateTransferCost(parent, child));
            }
        }
    }
    public double computeFaultRate(double freq)
    {
        return .005*Math.pow(10, (1-freq)/(1-fre[3]));
    }
    public void assignFrequencies()
    {
        int vmNum = getVmList().size();
        Random random = new Random(System.currentTimeMillis());
        for(int i=0;i<vmNum;i++)
        {
            CondorVM vm = (CondorVM) getVmList().get(i);
            if(vm!=null&&freq!=null&&fre!=null)
            freq.put(vm, vm.getFrequency());
            else if(vm==null)System.out.println("NULL VM IS HERE");
            else if(freq==null)System.out.println("NULL freq IS HERE");
            else if(fre==null)System.out.println("NULL fre IS HERE");
        }
    }
    private double calculateTransferCost(Task parent, Task child) {
        List<FileItem> parentFiles = parent.getFileList();
        List<FileItem> childFiles = child.getFileList();

        double acc = 0.0;

        for (FileItem parentFile : parentFiles) {
            if (parentFile.getType() != Parameters.FileType.OUTPUT) {
                continue;
            }

            for (FileItem childFile : childFiles) {
                if (childFile.getType() == Parameters.FileType.INPUT
                        && childFile.getName().equals(parentFile.getName())) {
                    acc += childFile.getSize();
                    break;
                }
            }
        }

        //file Size is in Bytes, acc in MB
        acc = acc / Consts.MILLION;
        // acc in MB, averageBandwidth in Mb/s
        return acc * 8 / averageBandwidth;
    }
    private void calculateRanks() {
        for (Task task : getTaskList()) {
            calculateRank(task);
        }
    }
    private double calculateRank(Task task) {
        if (rank.containsKey(task)) {
            return rank.get(task);
        }

        double averageComputationCost = 0.0;

        for (Double cost : computationCosts.get(task).values()) {
            averageComputationCost += cost;
        }

        averageComputationCost /= computationCosts.get(task).size();

        double max = 0.0;
        for (Task child : task.getChildList()) {
            double childCost = transferCosts.get(task).get(child)
                    + calculateRank(child);
            max = Math.max(max, childCost);
        }

        rank.put(task, averageComputationCost + max);

        return rank.get(task);
    }
   /* public void estCal()
    {
        Queue<Task> q = new LinkedList<>();
        for(Task task : getTaskList())
        {
            if(task.getParentList().size()==0)
            {
                earliestStartTimes.put(task,0.0);
                q.add(task);
                System.out.println("ENTRY TASK="+task.getCloudletId());
            }
        }
        System.out.println("BFS=");
        while(q.size()>=0)
        {
            
            Task c=q.peek();
            System.out.print(c.getCloudletId()+" ");
            q.remove();
            double est=0.0;
            for(Task t : c.getParentList())
            {
                double averageComputationCost = 0.0;

                for (Double cost : computationCosts.get(t).values()) {
                    averageComputationCost += cost;
                }

                averageComputationCost /= computationCosts.get(t).size();

                double a=0.0;
                if(transferCosts.get(t).get(c)!=null&&earliestStartTimes.get(t)!=null)
                a=earliestStartTimes.get(t)+averageComputationCost+transferCosts.get(t).get(c);
                else if(earliestStartTimes.get(t)==null)System.out.println("NULL es"+t.getCloudletId());
                if(a>=est)est=a;
            }
            earliestStartTimes.put(c, est);
            for(Task t : c.getChildList())
            {
                q.add(t);
            }
        }
        System.out.println();
    }*/
    public double estCalUtil(Task task)
    {
        if(earliestStartTimes.get(task)!=null)return earliestStartTimes.get(task);
        System.out.println("Task EST="+task.getCloudletId());
        double est=0.0;
        for(Task t : task.getParentList())
        {
            double d=0.0;
            double averageComputationCost = 0.0;

                for (Double cost : computationCosts.get(t).values()) {
                    averageComputationCost += cost;
                }

                averageComputationCost /= computationCosts.get(t).size();

                //double a=0.0;
                //if(transferCosts.get(t).get(c)!=null&&earliestStartTimes.get(t)!=null)
                d=estCalUtil(t)+averageComputationCost+transferCosts.get(t).get(task);
                if(d>est)
                    est=d;
        }
        earliestStartTimes.put(task, est);
        return est;
    }
    public void estCal()
    {
        for(Task task : getTaskList())
        {
            if(task.getChildList().size()==0)
                estCalUtil(task);
        }
    }
    void eft_cal()
    {
        for(Task task : getTaskList())
        {
            double averageComputationCost = 0.0;

            for (Double cost : computationCosts.get(task).values()) {
                averageComputationCost += cost;
            }

            averageComputationCost /= computationCosts.get(task).size();
            earliestFinishTimes.put(task, earliestStartTimes.get(task)+averageComputationCost);
        }
    }
    class TaskComparator implements Comparator<Task> 
    {
    // override the compare() method
    public int compare(Task t1, Task t2)
    {
        if (rank.get(t1) == rank.get(t2)) {
            return 0;
        }
        else if (rank.get(t1) < rank.get(t2)) {
            return 1;
        }
        else {
            return -1;
        }
    }
    }
    public void label(Task t,ArrayList<Task> clust,Map<Task,Boolean> assign)
    {
        clust.add(t);
        assign.put(t,true);
        List<Task> l1=t.getChildList();
        Collections.sort(l1,new TaskComparator());
        for(Task task : l1)
        {
            boolean b=true;
            for(Task p : task.getParentList())
            {
                if(assign.get(p)==false)
                {
                    b=false;
                    break;
                }
            }
            if(assign.get(task)==true&&b==true)
            {
                label(task,clust,assign);
            }
        }
    }
    public void clustering()
    {
        int k=0;
        Map<Task,Boolean> assign;
        assign = new HashMap<>();
        for(Task task : getTaskList())
        {
            assign.put(task,false);
        }
        
        for(Task task : getTaskList())
        {
            if(assign.get(task)==false)
            {
                k++;
                ArrayList<Task> cl = new ArrayList<>();
                label(task,cl,assign);
                clusters.add(cl);
            }
            
        }
    }
    public boolean hasUnallocatedParent(Task task)
    {
        if(task==null)return false;
        for(Task p : task.getParentList())
        {
            if(allocate.get(p)==false)
            {
                return true;
            }
        }
        return false;
    }
    public Task getDecidingParent(Task task)
    {
        Task deciding=null;
        double val=0.0;
        for(Task p : task.getParentList())
        {
            if(allocate.get(p)==false)
            {
                double averageComputationCost = 0.0;

                for (Double cost : computationCosts.get(p).values()) {
                    averageComputationCost += cost;
                }

                averageComputationCost /= computationCosts.get(p).size();
                double de=earliestStartTimes.get(p)+averageComputationCost+transferCosts.get(p).get(task);
                if(val<=de)
                {
                    de=val;
                    deciding=p;
                }
            }
        }
        return deciding;
    }
    public void allocateSubTargetTime(ArrayList<Task> path)
    {
        if(path==null)return;
        int n=path.size();
        if(n==0)return;
        Task start=path.get(0),end=path.get(n-1);
        double pl=0;
        for(int i=0;i<n;i++)
        {
            double averageComputationCost = 0.0;
            try{
            for (Double cost : computationCosts.get(path.get(i)).values()) {
                averageComputationCost += cost;
            }}
            catch(NullPointerException e)
            {
                
            }
            if(path.get(i)!=null)
            averageComputationCost /= computationCosts.get(path.get(i)).size();
            if(i!=n-1)
                pl+=averageComputationCost+transferCosts.get(path.get(i)).get(path.get(i+1));
            else 
                pl+=averageComputationCost;
        }
        double totalAverageCompCost=0.0;
        for(int i=0;i<n;i++)
        {
            double averageComputationCost = 0.0;
            try{
            for (Double cost : computationCosts.get(path.get(i)).values()) {
                averageComputationCost += cost;
            }}
            catch(NullPointerException e)
            {
                
            }
            
            if(path.get(i)!=null)
            averageComputationCost /= computationCosts.get(path.get(i)).size();
            totalAverageCompCost+=averageComputationCost;
        }
        Double nf=0.0;
        if(lft.get(path.get(n-1))!=null&&earliestStartTimes.get(path.get(0))!=null)
        nf=lft.get(path.get(n-1))-earliestStartTimes.get(path.get(0))-pl;
        else if(earliestStartTimes.get(path.get(0))==null)System.out.println("EST NULL ENCOUNTERED");
        else if(lft.get(path.get(n-1))==null)System.out.println("LFT NULL ENCOUNTERED");
        nf/=totalAverageCompCost;
        for(int i=0;i<n;i++)
        {
            Double averageComputationCost = 0.0;
            if(path.get(i)!=null)
            for (Double cost : computationCosts.get(path.get(i)).values()) {
                averageComputationCost += cost;
            }
            if(path.get(i)!=null)
            averageComputationCost /= computationCosts.get(path.get(i)).size();
            fet.put(path.get(i),averageComputationCost+averageComputationCost*nf);
            if(path.get(i)!=null)
            subTargetTime.put(path.get(i),earliestStartTimes.get(path.get(i))+fet.get(path.get(i)));
        }
    }
    public void distributeTargetTime(Task task)
    {
        System.out.println("DTT Called");
        if(task==null)return;
        while(hasUnallocatedParent(task))
        {
            ArrayList<Task> path=null;
            System.out.println("DTT while Called");
            Task temp=task;
            path = new ArrayList<Task>();
            while(hasUnallocatedParent(temp))
            {
                if(getDecidingParent(temp)!=null)
                {path.add(0, getDecidingParent(temp));
                temp=getDecidingParent(temp);}
                else break;
            }
            if(path.size()==0)break;
            allocateSubTargetTime(path);
            for(Task t : path)
            {
                allocate.put(t,true);
            }
            for(Task t : path)
            {
                if(t!=null)
                for(Task c : t.getChildList())
                {
                    Double d=0.0;
                    if(subTargetTime.get(t)!=null)
                    d=subTargetTime.get(t);
                    if(d>=earliestStartTimes.get(c))
                    earliestStartTimes.put(c,d);
                }
                if(t!=null)
                for(Task p : t.getParentList())
                {
                    Double d=0.0;
                    if(subTargetTime.get(t)!=null)
                    d=subTargetTime.get(t);
                    if(lft.get(p)==null||d>=lft.get(p))
                    lft.put(p,d);
                }
                if(t!=null)
                distributeTargetTime(t);
            }
        }
        
    }
    @Override
    public void run() throws Exception {
        for(Task task : getTaskList())
        {
            allocate.put(task,false);
        }
        for(Task t1 : getTaskList())
        {
         System.out.println("Parent="+t1.getCloudletId());
         for(Task c1 : t1.getChildList())
         {
             System.out.print(c1.getCloudletId()+" ");
         }
         System.out.println();
        }
        System.out.println("OKAY RUN");
        //assignFrequencies();
        System.out.println("OKAY freq");
        calculateComputationCosts();
        System.out.println("OKAY comp cost");
        calculateTransferCosts();
        System.out.println("OKAY transfer cost");
        estCal();
        System.out.println("OKAY EST");
        eft_cal();
        System.out.println("OKAY EFT");
        for(Task task : getTaskList())
        {
            if(task.getParentList().size()==0)
            {
                calculateRank(task);
            }
        }
        System.out.println("OKAY RANK");
        clustering();
        System.out.println("OKAY CLUSTER");
        for(Task task : getTaskList())
        {
            if(task.getChildList().size()==0)
            {
                subTargetTime.put(task, deadline);
                allocate.put(task,true);
            }
        }
        System.out.println("OKAY");
        for(Task task : getTaskList())
        {
            if(task.getChildList().size()==0)
            {
                lft.put(task, deadline);
                distributeTargetTime(task);
            }
        }
        System.out.println("Done");
        Map<CondorVM,Double> free=new HashMap<>();
        for(int i=0;i<getVmList().size();i++)
        {
            CondorVM vm=(CondorVM)getVmList().get(i);
            free.put(vm,0.0);
        }
        for(ArrayList<Task> clust : clusters)
        {
            double finish=0.0;
            for(Task t : clust)
            {
                double averageComputationCost = 0.0;

                for (Double cost : computationCosts.get(t).values()) {
                    averageComputationCost += cost;
                }

                averageComputationCost /= computationCosts.get(t).size();
                finish+=averageComputationCost;
            }
            CondorVM vm=null;
            double reliability=0.0;
            int vmNum = getVmList().size();
            for(int i=0;i<vmNum;i++)
            {
                CondorVM vm_t = (CondorVM) getVmList().get(i);
                double fault_rate=0.0;
                if(freq.get(vm_t)!=null)
                    fault_rate=computeFaultRate(freq.get(vm_t));
                else
                    fault_rate=computeFaultRate(2.2*0.4);
                double reli=0.0;
                if(freq.get(vm_t)!=null)
                reli=Math.pow(2.7, 0-(fault_rate*(finish+free.get(vm_t)))/freq.get(vm_t));
                else
                reli=Math.pow(2.7, 0-(fault_rate*(finish+free.get(vm_t)))/(2.2*0.4));
                if(reli>reliability)
                {
                    reliability=reli;
                    vm=vm_t;
                }
            }
            free.put(vm,finish+free.get(vm));
            for(Task t : clust)
            {
                t.setVmId(vm.getId());
                //t.setVmFreq(freq.get(vm));
            }
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
