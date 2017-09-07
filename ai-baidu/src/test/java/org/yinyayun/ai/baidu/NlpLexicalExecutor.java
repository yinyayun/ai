/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.ai.baidu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
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
public class NlpLexicalExecutor {
    @Ignore
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
            Set<String> completes = readCompleteIds(new File(completeFile));
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

    public static void main(String[] args) throws IOException {
        new NlpLexicalExecutor().executorFromTxt();
    }

    public void executorFromTxt() throws IOException {
        String filePath = "C:/Users/yinyayun/Desktop/xyz咨询数据/清洗/xyz-corpus-20170829.txt";
        String lexicalFilePath = "C:/Users/yinyayun/Desktop/xyz咨询数据/词法分析/lexical-20170829.txt";
        String completeFilePath = "C:/Users/yinyayun/Desktop/xyz咨询数据/词法分析/complete.txt";
        int threadSize = 3;
        //
        File file = new File(filePath);
        File lexicalFile = new File(lexicalFilePath);
        File completeFile = new File(completeFilePath);
        //
        // FileUtils.deleteQuietly(lexicalFile);
        BufferedReader reader = null;
        //
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName("utf-8")),
                    1024 * 1024);
            TaskQueue queue = new TaskQueue(2000);
            TaskExecutor executor = new TaskExecutor(6);
            // 异步执行
            Set<String> completes = readCompleteIds(completeFile);
            SaveAction<TextEntity> saveAction = buildTxtSaveAction(lexicalFile, completeFile);
            Runnable[] runs = buildThreads(threadSize, queue, (x, y) -> x.lexical(y), saveAction, completes);
            executor.execut(runs, false);
            // 数据加载
            String line = null;
            long id = 0;
            while ((line = reader.readLine()) != null) {
                ++id;
                if (!completes.contains(id)) {
                    queue.add(new TextEntity(String.valueOf(id), "", line));
                }
                if (id % 10000 == 0) {
                    System.out.println("完成:" + id);
                }
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
            e.printStackTrace();
        }
    }
    private Long lock = 1l;

    private SaveAction<TextEntity> buildTxtSaveAction(File saveFile, File completeFilePath) {
        Charset charset = Charset.forName("UTF-8");
        return (x) -> {
            try {
                synchronized (lock) {
                    FileUtils.writeStringToFile(saveFile, x.getResponse().concat("\n"), charset, true);
                    FileUtils.writeStringToFile(completeFilePath, x.getId().concat("\n"), charset, true);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        };
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

    private Set<String> readCompleteIds(File completeFile) throws IOException {
        Set<String> completes = new HashSet<String>();
        if (completeFile.exists()) {
            List<String> lines = FileUtils.readLines(completeFile, Charset.forName("utf-8"));
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
