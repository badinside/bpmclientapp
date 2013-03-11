package soadev.bpmclient;

import java.util.List;

import oracle.bpel.services.bpm.common.IBPMContext;

import oracle.bpel.services.workflow.task.impl.TaskUtil;
import oracle.bpel.services.workflow.task.model.Task;

import oracle.bpm.services.instancemanagement.IInstanceManagementService;
import oracle.bpm.services.processmetadata.IProcessMetadataService;
import oracle.bpm.services.processmetadata.ProcessMetadataSummary;

public class GetInitiableTasks {
    public static void main(String[] args) {
        GetInitiableTasks client = new GetInitiableTasks();
        client.testGetInitiatiableTasks();
        client.testInitiateTask();
        
    }
    public void testGetInitiatiableTasks() {
        System.out.println(">>> getting initiable tasks");
        try {
            IProcessMetadataService service =
                Fixture.getBPMServiceClient().getProcessMetadataService();
            IBPMContext bpmContext = Fixture.getIBPMContext("pino", "password1");
            List<ProcessMetadataSummary> initiableTasks =  service.getInitiatableProcesses(bpmContext);
            for(ProcessMetadataSummary pms: initiableTasks){
                System.out.println(pms.getProjectName()+ "/" + pms.getProcessName());
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void testGetProcessMetadata(){
        try {
            String processName = "HelloWorldProcess";
            String compositeDN = "default/HelloWorldProject!1.0*soa_a164d265-e961-4afb-b44b-50ea95a06fa1";
            IBPMContext ctx = Fixture.getIBPMContext("pino", "password1");
            IProcessMetadataService service =
                Fixture.getBPMServiceClient().getProcessMetadataService();
            ProcessMetadataSummary pms = service.getProcessMetadataSummary(ctx, compositeDN, processName);
            System.out.println("compositeDN: " + pms.getCompositeDN());
            System.out.println("processName: " + pms.getProcessName());
            System.out.println("compositeName: " + pms.getCompositeName());
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        } 
    }
    public void testInitiateTask(){
        System.out.println(">>> initiating a task");
        try {
            IProcessMetadataService service =
                Fixture.getBPMServiceClient().getProcessMetadataService();
            IBPMContext bpmContext = Fixture.getIBPMContext("pino", "password1");
            List<ProcessMetadataSummary> initiableTasks =  service.getInitiatableProcesses(bpmContext);
            ProcessMetadataSummary pms = initiableTasks.get(0);//get the first initable task
            IInstanceManagementService ims =
                Fixture.getBPMServiceClient().getInstanceManagementService();
            Task task = ims.createProcessInstanceTask(bpmContext, pms.getCompositeDN()+"/"+pms.getProcessName());
            System.out.println(">>> task initiated");
            printTask(task);
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void printTask(Task task){
        System.out.println(TaskUtil.getInstance().toString(task));
    }

}
