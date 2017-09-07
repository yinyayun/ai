/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.ai.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * CombineFiles.java
 *
 * @author yinyayun
 */
public class CombineFiles {
    public static void main(String[] args) throws IOException {
        String[] dirs = {"C:/Users/yinyayun/Desktop/分词训练/ToRCH2009_UTF-8--20140720",
                "C:/Users/yinyayun/Desktop/分词训练/ToRCH2014_SEG_UTF-8", "C:/Users/yinyayun/Desktop/分词训练/人民日报"};
        String savePath = "C:/Users/yinyayun/Desktop/分词训练/combin.txt";
        FileUtils.deleteQuietly(new File(savePath));
        //
        List<File> files = new ArrayList<File>();
        for (String dir : dirs) {
            for (File file : new File(dir).listFiles()) {
                files.add(file);
            }
        }
        //
        long count = 0;
        List<String> allLines = new ArrayList<String>();
        for (File file : files) {
            List<String> lines = FileUtils.readLines(file, Charset.forName("utf-8"));
            for (String line : lines) {
                if (line.length() > 0)
                    allLines.add(line.replace("  ", "<SPLIT>").replace(" ", "<SPLIT>"));
                if ((++count) % 10000 == 0) {
                    System.out.println(count);
                }
            }
        }
        FileUtils.writeLines(new File(savePath), "utf-8", allLines, true);
    }
}
