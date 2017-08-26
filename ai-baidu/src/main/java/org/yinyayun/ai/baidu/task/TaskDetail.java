/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.ai.baidu.task;

import java.util.Set;
import java.util.function.BiFunction;

import org.apache.commons.lang.StringUtils;
import org.yinyayun.ai.baidu.BaiduNlpAnalysis;
import org.yinyayun.ai.utils.iface.SaveAction;

/**
 * TaskDetail.java
 *
 * @author yinyayun
 */
public class TaskDetail implements Runnable {
    private TaskQueue queue;
    private BaiduNlpAnalysis analysis;
    private BiFunction<BaiduNlpAnalysis, String, String> process;
    private SaveAction<TextEntity> saveAction;
    private Set<String> completeIds;

    public TaskDetail(TaskQueue queue, BaiduNlpAnalysis analysis, BiFunction<BaiduNlpAnalysis, String, String> process,
            SaveAction<TextEntity> saveAction, Set<String> completeIds) {
        this.queue = queue;
        this.analysis = analysis;
        this.process = process;
        this.saveAction = saveAction;
        this.completeIds = completeIds;
    }

    @Override
    public void run() {
        while (true) {
            TextEntity textEntity = queue.take();
            if (!completeIds.contains(textEntity.getId()) && StringUtils.isNotEmpty(textEntity.getText())) {
                String response = process.apply(analysis, textEntity.getText());
                if (response != null) {
                    textEntity.setResponse(response);
                    saveAction.save(textEntity);
                }
            }
        }
    }
}
