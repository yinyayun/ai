/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.ai.baidu.task;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * TaskExecutor.java
 *
 * @author yinyayun
 */
public class TaskExecutor {
    private ExecutorService executorService;

    public TaskExecutor(int poolSize) {
        this.executorService = Executors.newFixedThreadPool(poolSize);
    }

    /**
     * @param runs
     * @param wait 是否等待返回
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @SuppressWarnings("rawtypes")
    public void execut(Runnable[] runs, boolean wait) throws InterruptedException, ExecutionException {
        Future[] futures = new Future[runs.length];
        for (int i = 0; i < runs.length; i++) {
            futures[i] = executorService.submit(runs[i]);
        }
        if (wait) {
            for (Future future : futures) {
                future.get();
            }
        }
    }

    public void shutDown() {
        executorService.shutdown();
    }
}
