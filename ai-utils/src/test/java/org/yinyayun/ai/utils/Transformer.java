/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.ai.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Ignore;
import org.junit.Test;
import org.yinyayun.ai.utils.iface.PredecodeAction;
import org.yinyayun.ai.utils.iface.SaveAction;

import com.csvreader.CsvWriter;

/**
 * Transformer.java
 *
 * @author yinyayun
 */
public class Transformer {

    @Test
    public void transfomer1() throws IOException {
        String file = "C:/Users/yinyayun/Desktop/xyz咨询数据/wenda1.csv";
        // Predecode
        Map<String, PredecodeAction> fieldPredecodeActions = new LinkedHashMap<String, PredecodeAction>();
        fieldPredecodeActions.put("QUESTION_ID", (x) -> x);
        fieldPredecodeActions.put("QUESTION", (x) -> x);
        fieldPredecodeActions.put("ANSWER_CONTENT", (x) -> parser(x));
        // saveAction
        FileUtils.deleteQuietly(new File("C:/Users/yinyayun/Desktop/xyz咨询数据/xxx.csv"));
        CsvWriter csvWriter = new CsvWriter("C:/Users/yinyayun/Desktop/xyz咨询数据/xxxx.csv", ',', Charset.forName("GBK"));
        try {
            SaveAction<List<String>> saveAction = (x) -> save(x, csvWriter);
            CsvFileTransformer csvFileTransformer = new CsvFileTransformer(file, fieldPredecodeActions, saveAction);
            csvFileTransformer.transformer();
        }
        finally {
            csvWriter.close();
        }
    }

    @Ignore
    @Test
    // [CONTENT_ID, KEYWORDS, TITLE, SUBTITLE, TEXT]
    public void transfomer2() throws IOException {
        String file = "C:/Users/yinyayun/Desktop/xyz咨询数据/wenda2.csv";
        // Predecode
        Map<String, PredecodeAction> fieldPredecodeActions = new LinkedHashMap<String, PredecodeAction>();
        fieldPredecodeActions.put("CONTENT_ID", (x) -> x);
        fieldPredecodeActions.put("TITLE", (x) -> x);
        fieldPredecodeActions.put("TEXT", (x) -> parser(x));
        // saveAction
        FileUtils.deleteQuietly(new File("C:/Users/yinyayun/Desktop/xyz咨询数据/xxx.csv"));
        CsvWriter csvWriter = new CsvWriter("C:/Users/yinyayun/Desktop/xyz咨询数据/xxxx.csv", ',', Charset.forName("GBK"));
        try {
            SaveAction<List<String>> saveAction = (x) -> save(x, csvWriter);
            CsvFileTransformer csvFileTransformer = new CsvFileTransformer(file, fieldPredecodeActions, saveAction);
            csvFileTransformer.transformer();
        }
        finally {
            csvWriter.close();
        }
    }

    private String parser(String content) {
        StringBuilder builder = new StringBuilder();
        Document document = Jsoup.parse(content);
        Elements elements = document.getAllElements();
        for (Element element : elements) {
            String ownText = element.ownText().trim();
            if (ownText.length() > 0) {
                if (builder.length() > 0) {
                    builder.append("\n");
                }
                builder.append(element.text().replace("\r\n", "").replace("\n", ""));
            }
        }
        return builder.toString();
    }

    private void save(List<String> valus, CsvWriter csvWriter) {
        try {
            csvWriter.writeRecord(valus.toArray(new String[0]));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
