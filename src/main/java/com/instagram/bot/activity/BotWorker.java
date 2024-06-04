package com.instagram.bot.activity;

import com.instagram.bot.workflow.BotWorkflowsAbstractWorker;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class BotWorker extends BotWorkflowsAbstractWorker {

    protected BotWorker(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    @Override
    protected String getTaskQueue() {
        return BotActivity.TASK_QUEUE;
    }

    @Override
    protected Class<?>[] getWorkflowImplementationClasses() {
        return new Class[0];
    }

    @Override
    protected Object[] getActivityImplementations() {
        return new Object[]{
                applicationContext.getBean(BotActivity.class)
        };
    }

    @Override
    protected void beforeStart() {

    }

    @Override
    protected void afterStart() {

    }
}
