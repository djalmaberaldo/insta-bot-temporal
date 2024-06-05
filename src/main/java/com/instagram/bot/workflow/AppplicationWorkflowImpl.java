package com.instagram.bot.workflow;

import com.instagram.bot.activity.BotActivity;
import com.instagram.bot.workflow.api.ApplicationWorkflow;
import lombok.extern.slf4j.Slf4j;
import static io.temporal.workflow.Workflow.newActivityStub;

@Slf4j
public class AppplicationWorkflowImpl implements ApplicationWorkflow {

    private final BotActivity botActivity = newActivityStub(
            BotActivity.class,
            BotActivity.activityOptions());

    @Override
    public void process() {
        log.info("Starting workflow...");
        var results = botActivity.getLatestCompetitions();
        var competiton = botActivity.readResultsByCompetiton(results.get(0));
        botActivity.processByCompetition(competiton);

    }

    @Override
    public void stop() {

    }
}
