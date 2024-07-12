package com.instagram.bot.workflow;

import com.instagram.bot.activity.BotActivity;
import com.instagram.bot.workflow.api.CompetitionWorkflow;
import lombok.extern.slf4j.Slf4j;
import static io.temporal.workflow.Workflow.newActivityStub;

@Slf4j
public class CompetitionWorkflowImpl implements CompetitionWorkflow {

    private final BotActivity botActivity = newActivityStub(
            BotActivity.class,
            BotActivity.activityOptions());


    @Override
    public void processCompetition(String competition) {

        var result = botActivity.readResultsByCompetiton(competition);
        botActivity.processByCompetition(result);
    }
}
