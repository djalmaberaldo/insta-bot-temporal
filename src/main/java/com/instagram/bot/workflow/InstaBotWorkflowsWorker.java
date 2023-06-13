package com.instagram.bot.workflow;

import com.instagram.bot.workflow.api.InstaWorkflow;
import net.bytebuddy.utility.RandomString;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowExecutionAlreadyStarted;
import lombok.extern.slf4j.Slf4j;
import static com.instagram.bot.workflow.api.InstaWorkflow.Options.WORKFLOWS_WORKER_TASK_QUEUE;

@Component
@Slf4j
public class InstaBotWorkflowsWorker extends InstaBotWorkflowsAbstractWorker {

    protected InstaBotWorkflowsWorker(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    @Override
    protected String getTaskQueue() {
        return WORKFLOWS_WORKER_TASK_QUEUE;
    }

    @Override
    protected Class<?>[] getWorkflowImplementationClasses() {
        return new Class[]{
                InstaWorkflowImpl.class
        };
    }

    @Override
    protected Object[] getActivityImplementations() {
        return new Object[]{};
    }

    @Override
    protected void beforeStart() {

    }

    @Override
    protected void afterStart() {
       startWorkflowIfNotStarted(InstaWorkflow.class,
               () -> WorkflowClient.start(() -> workflowClient.newWorkflowStub(
                       InstaWorkflow.class,
                       InstaWorkflow.Options.get(RandomString.make()))
                       .process()));
    }

    private <T> void startWorkflowIfNotStarted(Class<T> type, Starter starter) {
        try {
            starter.start();
        } catch (WorkflowExecutionAlreadyStarted alreadyStarted) {
            log.info("Workflow {} of type {} was already running under RunId {}, not restarting",
                    alreadyStarted.getExecution().getWorkflowId(),
                    alreadyStarted.getWorkflowType(),
                    alreadyStarted.getExecution().getRunId());
        }
    }

    interface Starter {
        WorkflowExecution start();
    }
}
