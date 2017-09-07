/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.ai.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.function.Function;

import org.apache.commons.lang.StringUtils;
import org.yinyayun.ai.utils.iface.SaveAction;

/**
 * BDJsonToText.java 百度返回的
 * 
 * @author yinyayun
 */
public class BDJsonToText<T> {
    private String file;
    private SaveAction<T> saveAction;
    private Function<String, T> parserFunction;

    public BDJsonToText(String file, Function<String, T> parserFunction, SaveAction<T> saveAction) {
        this.file = file;
        this.parserFunction = parserFunction;
        this.saveAction = saveAction;
    }

    public void process() throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(new File(file)), Charset.forName("utf-8")), 1024 * 1024)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (StringUtils.isNotEmpty(line)) {
                    T lines = parserFunction.apply(line);
                    saveAction.save(lines);
                }
            }

        }
    }
}
