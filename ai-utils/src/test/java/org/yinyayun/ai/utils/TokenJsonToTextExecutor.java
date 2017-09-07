/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.ai.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.yinyayun.ai.utils.iface.SaveAction;

/**
 * BDJsonToTextExecutor.java
 *
 * @author yinyayun
 */
public class TokenJsonToTextExecutor {

    @Test
    public void executor() throws IOException {
        String[] files = {"C:/Users/yinyayun/Desktop/xyz咨询数据/词法分析/lexical-1.txt",
                "C:/Users/yinyayun/Desktop/xyz咨询数据/词法分析/lexical-2.txt"};
        String saveFilePath = "C:/Users/yinyayun/Desktop/xyz咨询数据/词法分析/lexical-corpus1.txt";
        File saveFile = new File(saveFilePath);
        FileUtils.deleteQuietly(saveFile);
        Function<String, List<String>> parserFunction = parserFunction();
        SaveAction<List<String>> saveAction = saveAction(saveFile);
        for (String file : files) {
            BDJsonToText<List<String>> bdJsonToText = new BDJsonToText<List<String>>(file, parserFunction, saveAction);
            bdJsonToText.process();
        }
    }

    public final static Set<String> sentences = new HashSet<String>(
            Arrays.asList(new String[]{"。", "：", ":", ";", "；", "？", "?"}));

    private SaveAction<List<String>> saveAction(File saveFile) {
        return (x) -> {
            try {
                if (x != null && x.size() > 0)
                    FileUtils.writeLines(saveFile, "utf-8", x, "\n", true);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private Function<String, List<String>> parserFunction() {
        return (x) -> {
            List<String> lines = new ArrayList<String>();
            try {
                String del = "<SPLIT>";
                StringBuilder builder = new StringBuilder();
                JSONObject jsonObject = new JSONObject(x);
                JSONArray array = jsonObject.getJSONArray("items");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject item = array.getJSONObject(i);
                    String word = preParser(item.getString("item")).replace(" ", "");
                    if (word.length() == 0) {
                        continue;
                    }
                    if ("\n".equals(word)) {
                        lines.add(builder.toString());
                        builder.setLength(0);
                        continue;
                    }
                    else if (sentences.contains(word)) {
                        builder.append(word).append(del);
                        lines.add(builder.toString());
                        builder.setLength(0);
                        continue;
                    }
                    else if ("新一".equals(word) && (i + 1) < array.length()) {
                        String nextWord = array.getJSONObject(i + 1).getString("item").trim();
                        if ("站".equals(nextWord)) {
                            builder.append(word).append(nextWord).append(del);
                            i++;
                        }
                    }
                    builder.append(word).append(del);
                }
                if (builder.length() > 0) {
                    lines.add(builder.toString());
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return lines;
        };
    }

    private static String preParser(String word) {
        return word.replace("　", "").trim();
    }
}
