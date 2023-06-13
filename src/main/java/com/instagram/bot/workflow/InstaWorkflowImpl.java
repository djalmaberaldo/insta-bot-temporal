package com.instagram.bot.workflow;

import com.instagram.bot.activity.InstaBotActivity;
import com.instagram.bot.workflow.api.InstaWorkflow;
import static io.temporal.workflow.Workflow.newActivityStub;

public class InstaWorkflowImpl implements InstaWorkflow {

    private final InstaBotActivity instaBotActivity = newActivityStub(
            InstaBotActivity.class,
            InstaBotActivity.activityOptions());

    @Override
    public void process() {
        instaBotActivity.login();
    }

    @Override
    public void stop() {

    }
}
