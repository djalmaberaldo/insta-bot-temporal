package com.instagram.bot.workflow;

import com.instagram.bot.activity.BotActivity;
import com.instagram.bot.workflow.api.ApplicationWorkflow;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowExecutionAlreadyStarted;
import lombok.extern.slf4j.Slf4j;
import static com.instagram.bot.workflow.api.ApplicationWorkflow.Options.WORKFLOWS_WORKER_TASK_QUEUE;

@Component
@Slf4j
public class BotWorkflowsWorker extends BotWorkflowsAbstractWorker {

    protected BotWorkflowsWorker(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    @Override
    protected String getTaskQueue() {
        return WORKFLOWS_WORKER_TASK_QUEUE;
    }

    @Override
    protected Class<?>[] getWorkflowImplementationClasses() {
        return new Class[]{
                AppplicationWorkflowImpl.class,
                CompetitionWorkflowImpl.class
        };
    }

    @Override
    protected Object[] getActivityImplementations() {
        return new Object[]{
                applicationContext.getBean(BotActivity.class),
        };
    }

    @Override
    protected void beforeStart() {

    }

    @Override
    protected void afterStart() {
       startWorkflowIfNotStarted(ApplicationWorkflow.class,
               () -> WorkflowClient.start(() -> workflowClient.newWorkflowStub(
                       ApplicationWorkflow.class,
                       ApplicationWorkflow.Options.get())
                       .process()));
    }

    private <T> void startWorkflowIfNotStarted(Class<T> type, Starter starter) {
        try {
            WorkflowExecution execution = starter.start();
            log.info("Workflow {} of type {} started under RunId {}",
                    execution.getWorkflowId(),
                    type.getSimpleName(),
                    execution.getRunId());
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
