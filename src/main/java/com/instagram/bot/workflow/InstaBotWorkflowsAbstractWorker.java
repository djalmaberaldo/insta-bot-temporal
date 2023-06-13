package com.instagram.bot.workflow;

import com.uber.m3.tally.RootScopeBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import java.time.Duration;
import javax.annotation.PostConstruct;
import io.temporal.client.WorkflowClient;
import io.temporal.common.reporter.MicrometerClientStatsReporter;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import io.temporal.worker.WorkerOptions;


public abstract class InstaBotWorkflowsAbstractWorker {
    protected final ApplicationContext applicationContext;
    protected WorkflowClient workflowClient;
    protected Worker worker;

    @Value("localhost:7233")
    private String temporalUrl;


    protected InstaBotWorkflowsAbstractWorker(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    protected abstract String getTaskQueue();

    protected abstract Class<?>[] getWorkflowImplementationClasses();

    protected abstract Object[] getActivityImplementations();

    protected abstract void beforeStart();

    protected abstract void afterStart();

    @PostConstruct
    public void start() {
        WorkflowServiceStubs service = WorkflowServiceStubs.newServiceStubs(WorkflowServiceStubsOptions.newBuilder()
                .setTarget(temporalUrl)
                .setEnableHttps(false)
                .setRpcLongPollTimeout(Duration.ofSeconds(55)) // Some component on the route dislikes >60
                .validateAndBuildWithDefaults());
        workflowClient = WorkflowClient.newInstance(service);
        WorkerFactory factory = WorkerFactory.newInstance(workflowClient);
        worker = factory.newWorker(getTaskQueue(), WorkerOptions.newBuilder()
                .setMaxConcurrentLocalActivityExecutionSize(1)
                .validateAndBuildWithDefaults());
        worker.registerWorkflowImplementationTypes(getWorkflowImplementationClasses());
        worker.registerActivitiesImplementations(getActivityImplementations());
        beforeStart();
        factory.start();
        afterStart();
    }
}
