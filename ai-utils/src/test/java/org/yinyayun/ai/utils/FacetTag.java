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

import org.apache.commons.lang.StringUtils;

/**
 * FacetTag.java
 *
 * @author yinyayun
 */
public class FacetTag {
    public static void main(String[] args) {
        String file = "C:/Users/yinyayun/Desktop/xyz咨询数据/词性识别/pos-combin.txt";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(file))),
                1024 * 1024)) {
            String line = null;
            Map<String, Integer> counts = new HashMap<String, Integer>();
            while ((line = reader.readLine()) != null) {
                if (StringUtils.isEmpty(line)) {
                    continue;
                }
                String[] terms = line.split(" ");
                for (String term : terms) {
                    String[] parts = term.split("_");
                    if (parts.length == 2 && "w".equals(parts[1])) {
                        Integer count = counts.get(parts[0]);
                        if (count == null) {
                            count = 0;
                        }
                        counts.put(parts[0], ++count);
                    }
                }

            }
            counts.forEach((k, v) -> System.out.println(k + "=" + v));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
