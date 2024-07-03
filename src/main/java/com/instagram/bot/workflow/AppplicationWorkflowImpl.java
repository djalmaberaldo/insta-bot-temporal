package com.instagram.bot.workflow;

import com.instagram.bot.activity.BotActivity;
import com.instagram.bot.workflow.api.ApplicationWorkflow;
import java.time.Duration;
import io.temporal.workflow.Workflow;
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
        var competitions = botActivity.getLatestCompetitions();
        int count = 0;
        for(String competition : competitions) {
            var result = botActivity.readResultsByCompetiton(competition);
            botActivity.processByCompetition(result);
            count ++;
            Workflow.sleep(Duration.ofMinutes(1));

        }
//

    }

    @Override
    public void stop() {

    }
}
