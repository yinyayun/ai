/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.ai.utils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.yinyayun.ai.utils.iface.PredecodeAction;
import org.yinyayun.ai.utils.iface.SaveAction;

/**
 * Transformer.java
 *
 * @author yinyayun
 */
public class CsvTransformerText {
    public final Set<String> ss = new HashSet<String>(
            Arrays.asList(new String[]{"。", ".", "，", ",", "？", "?", "!", "！", ";", "；", ":", "："}));

    @Test
    public void transfomer1() throws IOException {
        // "C:/Users/yinyayun/Desktop/xyz咨询数据/wenda2.csv";
        String[] files = {"C:/Users/yinyayun/Desktop/xyz咨询数据/原始数据/wenda1.csv",
                "C:/Users/yinyayun/Desktop/xyz咨询数据/原始数据/wenda2.csv"};
        // Predecode
        Map<Integer, PredecodeAction> fieldPredecodeActions = new LinkedHashMap<Integer, PredecodeAction>();
        fieldPredecodeActions.put(4, (x) -> parser(x));
        // saveAction
        File saveFile = new File("C:/Users/yinyayun/Desktop/xyz咨询数据/清洗数据/xyz-corpus.txt");
        FileUtils.deleteQuietly(saveFile);
        SaveAction<List<String>> saveAction = (x) -> saveAsTxt(x, saveFile);
        for (String file : files) {
            CsvFileTransformer csvFileTransformer = new CsvFileTransformer(file, fieldPredecodeActions, saveAction);
            csvFileTransformer.transformer();
        }
    }

    private String parser(String content) {
        StringBuilder builder = new StringBuilder();
        Document document = Jsoup.parse(content);
        Elements elements = document.getAllElements();
        for (Element element : elements) {
            String ownText = element.ownText().replace("　", "").replace(" ", "").replace("\r\n", "").replace("\n", "")
                    .trim();
            if (ownText.length() > 0) {
                String last = ownText.substring(ownText.length() - 1);
                if (!ss.contains(last)) {
                    builder.append("。");
                }
                builder.append(ownText);
            }
        }
        return builder.toString();
    }

    private void saveAsTxt(List<String> lines, File file) {
        try {
            FileUtils.writeLines(file, "utf-8", lines, true);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
