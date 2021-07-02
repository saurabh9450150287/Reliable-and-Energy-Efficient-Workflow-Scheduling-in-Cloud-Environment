/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Copyright 2012-2013 University Of Southern California
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package workflowsim.examples.planning;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.CloudSim;
import org.workflowsim.CondorVM;
import org.workflowsim.WorkflowDatacenter;
import org.workflowsim.Job;
import org.workflowsim.WorkflowEngine;
import org.workflowsim.WorkflowPlanner;
import workflowsim.examples.WorkflowSimBasicExample1;
import org.workflowsim.utils.ClusteringParameters;
import org.workflowsim.utils.OverheadParameters;
import org.workflowsim.utils.Parameters;
import org.workflowsim.utils.ReplicaCatalog;
import org.cloudbus.output.Output;
import java.util.Random;
/**
 * This DHEFTPlanningAlgorithmExample1 creates a workflow planner, a workflow
 * engine, and one schedulers, one data centers and 20 heterogeneous vms that
 * has different communication cost (such that HEFT algorithm should work)
 *
 * @author Weiwei Chen
 * @since WorkflowSim Toolkit 1.1
 * @date Nov 9, 2013
 */
public class IREEWSPlanningAlgorithmExample extends WorkflowSimBasicExample1 {

    ////////////////////////// STATIC METHODS ///////////////////////
    protected static List<CondorVM> createVM(int userId, int vms) {

        //Creates a container to store VMs. This list is passed to the broker later
        LinkedList<CondorVM> list = new LinkedList<>();

        //VM Parameters
        long size = 10000; //image size (MB)
        int ram = 512; //vm memory (MB)
        int mips = 1000;
        long bw = 1000;
        int pesNumber = 1; //number of cpus
        String vmm = "Xen"; //VMM name

        //create VMs
        CondorVM[] vm = new CondorVM[vms];
        Random bwRandom = new Random(System.currentTimeMillis());
        for (int i = 0; i < vms; i++) {
            double ratio = bwRandom.nextDouble();
            vm[i] = new CondorVM(i, userId, mips * ratio, pesNumber, ram, (long) (bw * ratio), size, vmm, new CloudletSchedulerSpaceShared());
            list.add(vm[i]);
        }
        return list;
    }

    /**
     * Creates main() to run this example This example has only one datacenter
     * and one storage
     */
    public static void main(String[] args) {

        try {
            String[][] opt=new String[2000][4];
            String []type={"C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.100.0.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.100.1.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.100.10.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.100.11.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.100.12.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.100.13.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.100.14.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.100.15.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.100.16.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.100.17.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.100.18.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.100.19.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.100.2.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.100.3.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.100.4.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.100.5.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.100.6.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.100.7.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.100.8.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.100.9.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.1000.0.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.1000.1.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.1000.10.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.1000.11.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.1000.12.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.1000.13.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.1000.14.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.1000.15.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.1000.16.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.1000.17.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.1000.18.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.1000.19.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.1000.2.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.1000.3.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.1000.4.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.1000.5.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.1000.6.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.1000.7.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.1000.8.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.1000.9.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.200.0.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.200.1.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.200.10.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.200.11.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.200.12.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.200.13.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.200.14.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.200.15.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.200.16.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.200.17.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.200.18.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.200.19.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.200.2.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.200.3.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.200.4.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.200.5.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.200.6.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.200.7.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.200.8.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.200.9.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.300.0.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.300.1.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.300.10.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.300.11.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.300.12.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.300.13.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.300.14.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.300.15.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.300.16.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.300.17.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.300.18.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.300.19.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.300.2.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.300.3.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.300.4.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.300.5.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.300.6.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.300.7.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.300.8.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.300.9.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.400.0.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.400.1.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.400.10.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.400.11.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.400.12.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.400.13.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.400.14.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.400.15.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.400.16.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.400.17.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.400.18.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.400.19.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.400.2.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.400.3.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.400.4.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.400.5.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.400.6.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.400.7.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.400.8.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.400.9.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.50.0.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.50.1.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.50.10.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.50.11.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.50.12.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.50.13.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.50.14.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.50.15.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.50.16.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.50.17.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.50.18.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.50.19.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.50.2.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.50.3.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.50.4.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.50.5.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.50.6.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.50.7.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.50.8.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.50.9.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.500.0.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.500.1.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.500.10.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.500.11.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.500.12.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.500.13.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.500.14.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.500.15.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.500.16.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.500.17.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.500.18.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.500.19.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.500.2.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.500.3.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.500.4.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.500.5.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.500.6.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.500.7.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.500.8.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.500.9.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.600.0.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.600.1.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.600.10.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.600.11.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.600.12.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.600.13.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.600.14.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.600.15.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.600.16.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.600.17.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.600.18.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.600.19.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.600.2.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.600.3.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.600.4.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.600.5.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.600.6.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.600.7.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.600.8.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.600.9.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.700.0.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.700.1.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.700.10.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.700.11.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.700.12.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.700.13.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.700.14.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.700.15.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.700.16.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.700.17.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.700.18.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.700.19.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.700.2.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.700.3.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.700.4.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.700.5.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.700.6.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.700.7.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.700.8.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.700.9.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.800.0.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.800.1.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.800.10.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.800.11.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.800.12.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.800.13.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.800.14.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.800.15.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.800.16.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.800.17.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.800.18.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.800.19.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.800.2.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.800.3.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.800.4.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.800.5.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.800.6.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.800.7.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.800.8.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.800.9.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.900.0.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.900.1.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.900.10.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.900.11.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.900.12.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.900.13.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.900.14.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.900.15.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.900.16.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.900.17.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.900.18.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.900.19.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.900.2.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.900.3.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.900.4.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.900.5.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.900.6.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.900.7.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.900.8.dax", "C:\\Users\\Saurabh\\Documents\\SyntheticWorkflowstar\\SyntheticWorkflows\\LIGO\\LIGO.n.900.9.dax"};
            // First step: Initialize the WorkflowSim package. 

            /**
             * However, the exact number of vms may not necessarily be vmNum If
             * the data center or the host doesn't have sufficient resources the
             * exact vmNum would be smaller than that. Take care.
             */
            int vmNums[] = {4,8,16,32};//number of vms;
            /**
             * Should change this based on real physical path
             */
            int k1=0;
            int  vmNum;
            for(int i : vmNums)
            {    vmNum=i;
            Map<String,Double>mp_e=new HashMap<>();
            Map<String,Double>mp_r=new HashMap<>();
            
            for(String st : type)
            {
            String daxPath = st;//"C:\\Users\\Saurabh\\Documents\\WorkflowSim-1.0-master\\WorkflowSim-1.0-master\\config\\dax\\Inspiral_30.xml";

            File daxFile = new File(daxPath);
            if (!daxFile.exists()) {
                Log.printLine("Warning: Please replace daxPath with the physical path in your working environment!");
                return;
            }

            /**
             * Since we are using HEFT planning algorithm, the scheduling
             * algorithm should be static such that the scheduler would not
             * override the result of the planner
             */
            Parameters.SchedulingAlgorithm sch_method = Parameters.SchedulingAlgorithm.STATIC;
            Parameters.PlanningAlgorithm pln_method = Parameters.PlanningAlgorithm.IREEWS;
            ReplicaCatalog.FileSystem file_system = ReplicaCatalog.FileSystem.LOCAL;

            /**
             * No overheads
             */
            OverheadParameters op = new OverheadParameters(0, null, null, null, null, 0);

            /**
             * No Clustering
             */
            ClusteringParameters.ClusteringMethod method = ClusteringParameters.ClusteringMethod.NONE;
            ClusteringParameters cp = new ClusteringParameters(0, 0, method, null);
            
            /**
             * Initialize static parameters
             */
            Parameters.init(vmNum, daxPath, null,
                    null, op, cp, sch_method, pln_method,
                    null, 0);
            ReplicaCatalog.init(file_system);

            // before creating any entities.
            int num_user = 1;   // number of grid users
            Calendar calendar = Calendar.getInstance();
            boolean trace_flag = false;  // mean trace events

            // Initialize the CloudSim library
            CloudSim.init(num_user, calendar, trace_flag);

            WorkflowDatacenter datacenter0 = createDatacenter("Datacenter_0");

            /**
             * Create a WorkflowPlanner with one schedulers.
             */
            WorkflowPlanner wfPlanner = new WorkflowPlanner("planner_0", 1);
            /**
             * Create a WorkflowEngine.
             */
            
            WorkflowEngine wfEngine = wfPlanner.getWorkflowEngine();
            /**
             * Create a list of VMs.The userId of a vm is basically the id of
             * the scheduler that controls this vm.
             */
            
            List<CondorVM> vmlist0 = createVM(wfEngine.getSchedulerId(0), Parameters.getVmNum());
            
            /**
             * Submits this list of vms to this WorkflowEngine.
             */
            wfEngine.submitVmList(vmlist0, 0);

            /**
             * Binds the data centers with the scheduler.
             */
            wfEngine.bindSchedulerDatacenter(datacenter0.getId(), 0);
            System.out.println("OKAY");
            CloudSim.startSimulation();
            System.out.println("OKAY");
            List<Job> outputList0 = wfEngine.getJobsReceivedList();
            CloudSim.stopSimulation();
            printJobList(outputList0);
            DecimalFormat dft = new DecimalFormat("###.##");
            opt[k1][0]=st;
            opt[k1][1]=Integer.toString(vmNum);
            opt[k1][2]=Double.toString(Output.reliability);
            opt[k1][3]=Double.toString(Output.energy);
            k1++;
            System.out.println("K1="+k1);
            mp_e.put(st, Output.energy);
            mp_r.put(st, Output.reliability);
           // dft.format(job.getActualCPUTime());
        }
        }
        for(int i=0;i<k1;i++)
        {
            System.out.println(opt[i][0].substring(73)+"      "+opt[i][1]+"     "+opt[i][2]+"      "+opt[i][3]);
        }
        System.out.println();
        System.out.println();
       
        
        } catch (Exception e) {
            Log.printLine("The simulation has been terminated due to an unexpected error"+e);
        }
    }
}

