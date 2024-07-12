package com.instagram.bot.workflow;

import com.instagram.bot.activity.BotActivity;
import com.instagram.bot.workflow.api.ApplicationWorkflow;
import com.instagram.bot.workflow.api.CompetitionWorkflow;
import io.temporal.workflow.Async;
import lombok.extern.slf4j.Slf4j;
import static io.temporal.workflow.Workflow.getWorkflowExecution;
import static io.temporal.workflow.Workflow.newActivityStub;
import static io.temporal.workflow.Workflow.newChildWorkflowStub;

@Slf4j
public class AppplicationWorkflowImpl implements ApplicationWorkflow {

    private final BotActivity botActivity = newActivityStub(
            BotActivity.class,
            BotActivity.activityOptions());

    @Override
    public void process() {
        log.info("Starting workflow...");
        var competitions = botActivity.getLatestCompetitions();
        for (String competition : competitions) {

            var competitionWorkflow = newChildWorkflowStub(
                    CompetitionWorkflow.class,
                    CompetitionWorkflow.Options.get(competition));
            Async.procedure(competitionWorkflow::processCompetition, competition);
            getWorkflowExecution(competitionWorkflow).get();
        }
    }

    @Override
    public void stop() {

    }
}
