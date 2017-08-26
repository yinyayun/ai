/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.ai.baidu.task;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * TaskQueue.java
 *
 * @author yinyayun
 */
public class TaskQueue {
    private BlockingQueue<TextEntity> taskQueue;

    public TaskQueue(int queueSize) {
        this.taskQueue = new LinkedBlockingDeque<TextEntity>(queueSize);
    }

    public TextEntity take() {
        try {
            return taskQueue.take();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void add(TextEntity entity) {
        try {
            taskQueue.put(entity);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isEmpty() {
        return taskQueue.isEmpty();
    }
}
