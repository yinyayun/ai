/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.ai.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

/**
 * CleanDirtyPosData.java
 *
 * @author yinyayun
 */
public class CleanDirtyPosData {
    public static void main(String[] args) throws IOException {
        String file = "C:/Users/yinyayun/Desktop/xyz咨询数据/词性识别/pos-combin.txt";
        String saveFile = "C:/Users/yinyayun/Desktop/xyz咨询数据/词性识别/pos-combin-clean.txt";
        FileUtils.deleteQuietly(new File(saveFile));
        Set<String> errorWPos = new HashSet<String>(Arrays.asList(new String[]{"小", "尔", "4", "8546279", "7", "8",
                "zrq", "9", "8980099", "8980099", "A", "6951553", "あ", "C", "ぃ", "D", "E", "F", "う", "え", "I", "お", "J",
                "K", "か", "L", "が", "き", "6951557", "O", "く", "8198928", "Q", "け", "こ", "ご", "U", "ざ", "し", "W", "X",
                "す", "じ", "せ", "ぞ", "た", "っ", "つ", "て", "で", "と", "ど", "な", "j", "l", "ね", "の", "は", "o", "ひ", "s", "ふ",
                "x", "2030601", "5678197", "8198919", "4092371", "み", "め", "も", "ゃ", "预", "よ", "ら", "り", "れ", "ろ", "わ",
                "ん", "6801662", "8616263", "6357722", "国寿", "械", "岳", "Viewing", "84886870", "2037662", "5899032",
                "6366333", "2212221", "6369713", "6369712", "84886888", "ヾ", "HK", "℅", "88665021", "甍", "7545368",
                "16888000", "2223803", "4229087", "5007007", "奈", "8730782", "兰", "关", "ⅹ", "85290000", "8695511",
                "8386063", "7193333", "zhongsy", "切", "控股", "庞大", "险", "Zulassungsbescheid", "xiaoyanzi", "zjj", "ｙ",
                "较", "殊", "㎎", "林", "㎝", "㎡", "讯", "￠", "￡", "询", "培"}));

        //
        // 误识别为标点，但是应该为数字
        Set<String> replaceW2MWords = new HashSet<String>(Arrays.asList(
                new String[]{"①", "②", "③", "④", "⑤", "⑥", "⑦", "⑧", "⑨", "⑩", "⑴", "⑵", "⑶", "⑷", "⑸", "⑹", "⑺", "⑻",
                        "⑼", "⑽", "⑾", "⑿", "⒀", "⒁", "⒈", "⒉", "⒊", "⒋", "⒌", "⒍", "⒎", "⒏", "⒐", "Ⅰ", "Ⅱ", "Ⅲ", "Ⅳ",
                        "Ⅴ", "Ⅵ", "Ⅷ", "Ⅸ", "ⅰ", "ⅱ", "ⅲ", "ⅴ", "ⅶ", "㈠", "㈡", "㈢", "㈣", "㈤", "㈥", "㈦", "㈧", "㈨"}));

        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)), 1024 * 1024);
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(saveFile), "utf-8"), 1024 * 1024);
            String line = null;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                if (StringUtils.isEmpty(line)) {
                    continue;
                }
                builder.setLength(0);
                String[] terms = line.split(" ");
                for (String term : terms) {
                    String[] parts = term.split("_");
                    if (parts.length != 2) {
                        continue;
                    }
                    if ("w".equals(parts[1])) {
                        if (errorWPos.contains(parts[0])) {
                            builder.setLength(0);
                            break;
                        }
                        else if (replaceW2MWords.contains(parts[0])) {
                            if (builder.length() > 0) {
                                builder.append(" ");
                            }
                            builder.append(parts[0]).append("_").append("m");
                            continue;
                        }
                    }
                    if (builder.length() > 0) {
                        builder.append(" ");
                    }
                    builder.append(parts[0]).append("_").append(parts[1]);
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
            reader.close();
            writer.close();
        }
    }
}
