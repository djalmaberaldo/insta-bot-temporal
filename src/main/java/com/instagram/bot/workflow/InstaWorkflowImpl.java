package com.instagram.bot.workflow;

import com.instagram.bot.activity.InstaBotActivity;
import com.instagram.bot.workflow.api.InstaWorkflow;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import static io.temporal.workflow.Workflow.newActivityStub;

@Slf4j
public class InstaWorkflowImpl implements InstaWorkflow {

    private final InstaBotActivity instaBotActivity = newActivityStub(
            InstaBotActivity.class,
            InstaBotActivity.activityOptions());

    @Override
    public void process() {
        log.info("Starting workflow...");
        instaBotActivity.login();
    }

    @Override
    public void stop() {

    }
}
