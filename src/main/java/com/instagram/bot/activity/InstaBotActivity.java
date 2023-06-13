package com.instagram.bot.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import io.temporal.activity.ActivityOptions;
import static io.temporal.common.RetryOptions.newBuilder;
import static java.time.Duration.ofMinutes;
import static java.time.Duration.ofSeconds;

@ActivityInterface
public interface InstaBotActivity {

    String TASK_QUEUE = "jiraWorker-taskQueue";

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
    void login();

    @ActivityMethod
    void likeByTags(String... tags);

    @ActivityMethod
    void likeByFeed();
}
