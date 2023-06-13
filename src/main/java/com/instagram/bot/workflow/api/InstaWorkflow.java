package com.instagram.bot.workflow.api;

import java.time.Duration;
import io.temporal.client.WorkflowOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import lombok.NonNull;
import static io.temporal.api.enums.v1.WorkflowIdReusePolicy.WORKFLOW_ID_REUSE_POLICY_ALLOW_DUPLICATE;
import static java.time.Duration.ofMinutes;

@WorkflowInterface
public interface InstaWorkflow {

    interface Options {
        String WORKFLOWS_WORKER_TASK_QUEUE = "insta-bot-workflows-taskQueue";

        @NonNull
        static WorkflowOptions get(String id) {
            return WorkflowOptions.newBuilder()
                    .setTaskQueue(WORKFLOWS_WORKER_TASK_QUEUE)
                    .setWorkflowId(id)
                    .setWorkflowIdReusePolicy(WORKFLOW_ID_REUSE_POLICY_ALLOW_DUPLICATE)
                    .setRetryOptions(RetryOptions.newBuilder()
                            .setInitialInterval(Duration.ofSeconds(30))
                            .setBackoffCoefficient(2D)
                            .setMaximumInterval(ofMinutes(5))
                            .validateBuildWithDefaults())
                    .validateBuildWithDefaults();

        }
    }

    @WorkflowMethod
    void process();


    @SignalMethod
    void stop();
}
