/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.ai.baidu;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.yinyayun.ai.baidu.task.TaskDetail;
import org.yinyayun.ai.baidu.task.TaskExecutor;
import org.yinyayun.ai.baidu.task.TaskQueue;
import org.yinyayun.ai.baidu.task.TextEntity;
import org.yinyayun.ai.utils.iface.SaveAction;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

/**
 * BaiduNlpExecutor.java
 *
 * @author yinyayun
 */
public class NlpExecutor {
    @Test
    public void executor1() throws IOException {
        String file = "C:/Users/yinyayun/Desktop/xyz咨询数据/wenda2-transformer.csv";
        String lexicalFile = "C:/Users/yinyayun/Desktop/xyz咨询数据/wenda2-lexical.csv";
        String completeFile = "C:/Users/yinyayun/Desktop/xyz咨询数据/wenda2-complete.txt";
        int threadSize = 3;
        //
        FileUtils.deleteQuietly(new File(lexicalFile));
        CsvReader reader = new CsvReader(file, ',', Charset.forName("gbk"));
        CsvWriter csvWriter = new CsvWriter(lexicalFile, ',', Charset.forName("gbk"));
        //
        try {
            TaskQueue queue = new TaskQueue(2000);
            TaskExecutor executor = new TaskExecutor(6);
            // 异步执行
            Set<String> completes = readCompleteIds(completeFile);
            SaveAction<TextEntity> saveAction = buildSaveAction(csvWriter, completeFile);
            Runnable[] runs = buildThreads(threadSize, queue, (x, y) -> x.lexical(y), saveAction, completes);
            executor.execut(runs, false);
            // 数据加载
            while (reader.readRecord()) {
                String id = reader.get(0);
                String question = reader.get(1);
                String content = reader.get(2);
                queue.add(new TextEntity(id, question, content));
            }
            System.out.println("数据已经读完...");
            while (true) {
                Thread.sleep(1000);
                if (queue.isEmpty()) {
                    executor.shutDown();
                    break;
                }
            }
            System.out.println("退出...");
        }
        catch (Exception e) {
            reader.close();
            csvWriter.close();
            e.printStackTrace();
        }
    }

    private SaveAction<TextEntity> buildSaveAction(CsvWriter csvWriter, String completeFilePath) {
        File completeFile = new File(completeFilePath);
        Charset UTF = Charset.forName("utf-8");
        return (x) -> {
            String[] contents = {x.getId(), x.getTitle(), x.getResponse()};
            try {
                csvWriter.writeRecord(contents);
                FileUtils.writeStringToFile(completeFile, x.getId().concat("\n"), UTF, true);
                System.out.println("完成:" + x.getId());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    private Set<String> readCompleteIds(String completeFiles) throws IOException {
        Set<String> completes = new HashSet<String>();
        File file = new File(completeFiles);
        if (file.exists()) {
            List<String> lines = FileUtils.readLines(new File(completeFiles), Charset.forName("utf-8"));
            lines.forEach(x -> completes.add(x));
        }
        return completes;
    }

    /**
     * 创建nlp调用线程
     * 
     * @param threadSize
     * @param queue
     * @param process
     * @param saveAction
     * @return
     */
    private Runnable[] buildThreads(int threadSize, final TaskQueue queue,
            BiFunction<BaiduNlpAnalysis, String, String> process, SaveAction<TextEntity> saveAction,
            Set<String> completes) {
        BaiduNlpAnalysis analysis = new BaiduNlpAnalysis(new ClientFactory());
        Runnable[] runs = new Runnable[threadSize];
        for (int i = 0; i < runs.length; i++) {
            runs[i] = new TaskDetail(queue, analysis, process, saveAction, completes);
        }
        return runs;
    }
}
