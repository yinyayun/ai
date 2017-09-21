/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.ai.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

/**
 * RmrbPosTransforer.java
 *
 * @author yinyayun
 */
public class RmrbPosTransformer {
    public static void main(String[] args) {
        String sourceFile = "C:/Users/yinyayun/Desktop/xyz咨询数据/词性识别/人民日报.txt";
        String optFile = "C:/Users/yinyayun/Desktop/xyz咨询数据/词性识别/人民日报-标注统一.txt";
        BufferedReader reader = null;
        BufferedWriter writer = null;
        String line = null;
        StringBuilder builder = new StringBuilder();
        try {
            FileUtils.deleteQuietly(new File(optFile));
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(optFile), "utf-8"), 1024 * 1024);
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile), "utf-8"), 1024 * 1024);
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (StringUtils.isEmpty(line)) {
                    continue;
                }
                builder.setLength(0);
                String[] splits = line.split(" ");
                for (String split : splits) {
                    split = split.trim();
                    if (StringUtils.isEmpty(split)) {
                        continue;
                    }
                    String[] parts = split.split("/");
                    if (parts.length != 2) {
                        builder.setLength(0);
                        break;
                    }
                    String pos = parts[1].toLowerCase();
                    if (builder.length() > 0) {
                        builder.append(" ");
                    }
                    builder.append(parts[0]).append("_").append(pos);

                }
                if (builder.length() > 0) {
                    builder.append("\n");
                    writer.write(builder.toString());
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            close(reader);
            close(writer);
        }
    }

    public static void close(AutoCloseable closeable) {
        try {
            closeable.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
