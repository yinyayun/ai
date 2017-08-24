/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.ai.baidu.task;

import org.yinyayun.ai.baidu.BaiduNlpAnalysis;

/**
 * TaskDetail.java
 *
 * @author yinyayun
 */
public class TaskDetail implements Runnable {
    private TaskQueue queue;
    private BaiduNlpAnalysis analysis;

    public TaskDetail(TaskQueue queue, BaiduNlpAnalysis analysis) {
        this.queue = queue;
        this.analysis = analysis;
    }

    @Override
    public void run() {
        while (true) {
            TextEntity textEntity = queue.take();
            String leical = analysis.lexical(textEntity.getText());
            String parser = analysis.sentenceParser(textEntity.getText());
        }
    }
}
