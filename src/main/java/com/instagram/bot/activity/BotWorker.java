package com.instagram.bot.activity;

import com.instagram.bot.workflow.InstaBotWorkflowsAbstractWorker;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class BotWorker extends InstaBotWorkflowsAbstractWorker {

    protected BotWorker(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    @Override
    protected String getTaskQueue() {
        return InstaBotActivity.TASK_QUEUE;
    }

    @Override
    protected Class<?>[] getWorkflowImplementationClasses() {
        return new Class[0];
    }

    @Override
    protected Object[] getActivityImplementations() {
        return new Object[]{
                applicationContext.getBean(InstaBotActivity.class)
        };
    }

    @Override
    protected void beforeStart() {

    }

    @Override
    protected void afterStart() {

    }
}
