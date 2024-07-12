package com.instagram.bot.workflow.api;

import java.time.Duration;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.ChildWorkflowOptions;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import static com.instagram.bot.workflow.api.ApplicationWorkflow.Options.WORKFLOWS_WORKER_TASK_QUEUE;
import static io.temporal.api.enums.v1.ParentClosePolicy.PARENT_CLOSE_POLICY_ABANDON;
import static io.temporal.api.enums.v1.WorkflowIdReusePolicy.WORKFLOW_ID_REUSE_POLICY_ALLOW_DUPLICATE;
import static java.time.Duration.ofMinutes;

@WorkflowInterface
public interface CompetitionWorkflow {

    interface Options {
        static ChildWorkflowOptions get(String id) {
            return ChildWorkflowOptions.newBuilder()
                    .setTaskQueue(WORKFLOWS_WORKER_TASK_QUEUE)
                    .setWorkflowId(id)
                    .setWorkflowIdReusePolicy(WORKFLOW_ID_REUSE_POLICY_ALLOW_DUPLICATE)
                    .setParentClosePolicy(PARENT_CLOSE_POLICY_ABANDON)
                    .setRetryOptions(RetryOptions.newBuilder()
                            .setInitialInterval(Duration.ofSeconds(30))
                            .setBackoffCoefficient(2D)
                            .setMaximumInterval(ofMinutes(5))
                            .validateBuildWithDefaults())
                    .validateAndBuildWithDefaults();
        }
    }

    @WorkflowMethod
    void processCompetition(String uuid);


}
