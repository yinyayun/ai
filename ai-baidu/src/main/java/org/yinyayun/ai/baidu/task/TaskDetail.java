/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.ai.baidu.task;

import java.util.Set;
import java.util.function.BiFunction;

import org.apache.commons.lang.StringUtils;
import org.yinyayun.ai.baidu.api.BaiduApi;
import org.yinyayun.ai.baidu.api.BaiduNlpSDK;
import org.yinyayun.ai.utils.iface.SaveAction;

/**
 * TaskDetail.java
 *
 * @author yinyayun
 */
public class TaskDetail implements Runnable {
    private TaskQueue queue;
    private BaiduApi api;
    private BiFunction<BaiduApi, String, String> process;
    private SaveAction<TextEntity> saveAction;
    private Set<String> completeIds;

    public TaskDetail(TaskQueue queue, BaiduApi api, BiFunction<BaiduApi, String, String> process,
            SaveAction<TextEntity> saveAction, Set<String> completeIds) {
        this.queue = queue;
        this.api = api;
        this.process = process;
        this.saveAction = saveAction;
        this.completeIds = completeIds;
    }

    @Override
    public void run() {
        while (true) {
            TextEntity textEntity = queue.take();
            if (!completeIds.contains(textEntity.getId()) && StringUtils.isNotEmpty(textEntity.getText())) {
                String response = process.apply(api, textEntity.getText());
                if (response != null) {
                    textEntity.setResponse(response);
                    saveAction.save(textEntity);
                }
            }
        }
    }
}
