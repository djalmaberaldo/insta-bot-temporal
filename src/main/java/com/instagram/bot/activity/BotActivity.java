package com.instagram.bot.activity;

import com.instagram.bot.model.Competition;
import java.util.List;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import io.temporal.activity.ActivityOptions;
import static io.temporal.common.RetryOptions.newBuilder;
import static java.time.Duration.ofMinutes;
import static java.time.Duration.ofSeconds;

@ActivityInterface
public interface BotActivity {

    String TASK_QUEUE = "instaWorker-taskQueue";

    static ActivityOptions activityOptions() {
        return ActivityOptions.newBuilder()
                .setTaskQueue(TASK_QUEUE)
                .setStartToCloseTimeout(ofMinutes(1))
                .setRetryOptions(newBuilder()
                        .setInitialInterval(ofSeconds(5))
                        .setBackoffCoefficient(2D)
                        .setMaximumInterval(ofMinutes(5))
                        .build())
                .build();
    }

    @ActivityMethod
    List<String> getLatestCompetitions();

    @ActivityMethod
    Competition readResultsByCompetiton(String result);

    @ActivityMethod
    void processByCompetition() throws Exception;
}
