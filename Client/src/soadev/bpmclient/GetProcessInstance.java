package soadev.bpmclient;

import java.util.Calendar;
import java.util.List;

import oracle.bpel.services.bpm.common.IBPMContext;

import oracle.bpm.client.common.BPMServiceClientException;
import oracle.bpm.services.common.exception.BPMException;
import oracle.bpm.services.instancemanagement.IInstanceManagementService;
import oracle.bpm.services.instancemanagement.model.IIdentityType;
import oracle.bpm.services.instancemanagement.model.IProcessComment;
import oracle.bpm.services.instancemanagement.model.IProcessInstance;
import oracle.bpm.services.instancemanagement.model.impl.IdentityType;
import oracle.bpm.services.instancemanagement.model.impl.ProcessComment;
import oracle.bpm.services.instancequery.IInstanceQueryService;

public class GetProcessInstance {
    public static void main(String[] args) {
        GetProcessInstance client = new GetProcessInstance();
        client.testGetProcessInstance();
        client.testAddAttachment();
    }

    public void testGetProcessInstance() {
        try {
            String sampleProcessId = "150001"; //replace this with a valid id
            IBPMContext ctx = Fixture.getIBPMContext("pino", "password1");
            IInstanceQueryService queryService =
                Fixture.getBPMServiceClient().getInstanceQueryService();
            System.out.println("retrieve sample instance");
            IProcessInstance instance =
                queryService.getProcessInstance(ctx, sampleProcessId);

            printProcessInstance(instance);

        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
    }

    private void printProcessInstance(IProcessInstance instance) {
        System.out.println(instance.getSystemAttributes().getProcessInstanceId());
        System.out.println(instance.getSystemAttributes().getProcessNumber());
        System.out.println(instance.getSystemAttributes().getState());
        System.out.println(instance.getProcessDN());
        System.out.println(instance.getCreator());
        System.out.println(instance.getSystemAttributes().getCreatedDate().getTime());

    }
    
    public void testAddAttachment(){
        try {
            String sampleProcessId = "150001"; //replace this with a valid id
            IBPMContext ctx = Fixture.getIBPMContext("pino", "password1");
            IInstanceQueryService queryService =
                Fixture.getBPMServiceClient().getInstanceQueryService();
            IProcessInstance instance =
                queryService.getProcessInstance(ctx, sampleProcessId);
            IInstanceManagementService service =
                Fixture.getBPMServiceClient().getInstanceManagementService();
            IProcessComment comment = new ProcessComment();
            IIdentityType identity = new IdentityType();
            identity.setDisplayName(ctx.getUser());
            identity.setId(ctx.getUser());
            identity.setType("user");
            comment.setUpdatedBy(identity);
            comment.setComment("my programmatic comment5");
            comment.setUpdatedDate(Calendar.getInstance());
            service.addComment(ctx, instance, comment);
            //check if persisted
            IProcessInstance updatedInstance =
                queryService.getProcessInstance(ctx, sampleProcessId);
            List<IProcessComment> comments = updatedInstance.getUserComment();
            for (IProcessComment c : comments) {
                System.out.println(c.getComment());// the new comment should appear
            }
        } catch (BPMServiceClientException bpmsce) {
            // TODO: Add catch code
            bpmsce.printStackTrace();
        } catch (BPMException bpme) {
            // TODO: Add catch code
            bpme.printStackTrace();
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }

    }
}
