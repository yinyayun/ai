/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.ai.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

/**
 * BuilderPosDictory.java
 *
 * @author yinyayun
 */
public class BuilderPosDictory {
    public static void main(String[] args) {
        String sourceFile = "C:/Users/yinyayun/Desktop/xyz咨询数据/词性识别/pos-combin.txt";
        Map<String, Map<String, Integer>> wordTagCounts = new HashMap<String, Map<String, Integer>>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile), "utf-8"),
                1024 * 1024)) {
            String line = reader.readLine();
            while (line != null) {
                String[] tokens = line.split(" ");
                for (String token : tokens) {
                    if (token.trim().length() > 0) {
                        String[] parts = token.split("_");
                        if (parts.length == 2) {
                            Map<String, Integer> tags = wordTagCounts.get(parts[0]);
                            if (tags == null) {
                                tags = new HashMap<String, Integer>();
                                wordTagCounts.put(parts[0], tags);
                            }
                            Integer count = tags.get(parts[1]);
                            if (count == null) {
                                count = 0;
                            }
                            tags.put(parts[1], ++count);
                        }
                    }
                }
                line = reader.readLine();
            }
            JSONObject jsonObject = new JSONObject(wordTagCounts);
            FileUtils.writeStringToFile(new File("C:/Users/yinyayun/Desktop/xyz咨询数据/词性识别/pos-dictory.json"),
                    jsonObject.toString(), "utf-8", false);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
