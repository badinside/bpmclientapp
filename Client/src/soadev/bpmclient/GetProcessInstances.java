package soadev.bpmclient;

import java.util.ArrayList;
import java.util.List;

import oracle.bpel.services.bpm.common.IBPMContext;
import oracle.bpel.services.workflow.repos.Column;
import oracle.bpel.services.workflow.repos.Ordering;

import oracle.bpel.services.workflow.repos.Predicate;
import oracle.bpm.services.instancemanagement.model.IProcessInstance;
import oracle.bpm.services.instancequery.IColumnConstants;
import oracle.bpm.services.instancequery.IInstanceQueryInput;
import oracle.bpm.services.instancequery.IInstanceQueryService;
import oracle.bpm.services.instancequery.impl.InstanceQueryInput;

public class GetProcessInstances {
    
    public static void main(String[] args) {
        GetProcessInstances client = new GetProcessInstances();
        client.testGetProcessInstances();
    }
    
    public void testGetProcessInstances(){
        try {
            IInstanceQueryService queryService =
                Fixture.getBPMServiceClient().getInstanceQueryService();
            IBPMContext bpmContext = Fixture.getIBPMContext("pino", "password1");
            List<Column> columns = new ArrayList<Column>();
            columns.add(IColumnConstants.PROCESS_ID_COLUMN);
            columns.add(IColumnConstants.PROCESS_NUMBER_COLUMN);
            columns.add(IColumnConstants.PROCESS_STATE_COLUMN);
            columns.add(IColumnConstants.PROCESS_TITLE_COLUMN);
            columns.add(IColumnConstants.PROCESS_CREATOR_COLUMN);
            columns.add(IColumnConstants.PROCESS_CREATEDDATE_COLUMN);

            Ordering ordering = new Ordering(IColumnConstants.PROCESS_NUMBER_COLUMN, true, true);  
            Predicate pred = new Predicate(IColumnConstants.PROCESS_STATE_COLUMN,
                   Predicate.OP_EQ,
                   "OPEN");
            IInstanceQueryInput input = new InstanceQueryInput();
            input.setAssignmentFilter(IInstanceQueryInput.AssignmentFilter.MY_AND_GROUP);
            
            List<IProcessInstance> processInstances =
                queryService.queryInstances(bpmContext, columns, pred, ordering,
                                                   input);
            System.out.println("ProcessId\tProcess#\tState\tTitle\t\t\t\t\tCreator\tCreadedDate");
            for (IProcessInstance instance : processInstances) {
                System.out.println(instance.getSystemAttributes().getProcessInstanceId()
                                   + "\t" + instance.getSystemAttributes().getProcessNumber() 
                                   + "\t" + instance.getSystemAttributes().getState() 
                                   + "\t" + instance.getTitle()
                                   + "\t" + instance.getCreator()
                                   + "\t" + instance.getSystemAttributes().getCreatedDate().getTime());
            }
            if (processInstances.isEmpty()){
                System.out.println("no result");
            }
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
    }
}
