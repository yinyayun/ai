/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.ai.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.yinyayun.ai.utils.iface.PredecodeAction;
import org.yinyayun.ai.utils.iface.SaveAction;

import com.csvreader.CsvReader;

/**
 * CsvReader.java csv文件内容转换
 * 
 * @author yinyayun
 */
public class CsvFileTransformer {
    private String file;
    private SaveAction saveAction;
    private Map<String, PredecodeAction> fieldPredecode;
    private String encode;
    private char delimiter;

    /**
     * @param file
     * @param fieldPredecode 每个字段上的处理
     * @param saveAction 转换后如何保存
     */
    public CsvFileTransformer(String file, Map<String, PredecodeAction> fieldPredecode, SaveAction saveAction) {
        this(file, fieldPredecode, saveAction, ',', "gbk");
    }

    /**
     * @param file
     * @param fieldPredecode 每个字段上的处理
     * @param saveAction 转换后如何保存
     */
    public CsvFileTransformer(String file, Map<String, PredecodeAction> fieldPredecode, SaveAction saveAction,
            char delimiter, String encode) {
        this.file = file;
        this.saveAction = saveAction;
        this.fieldPredecode = fieldPredecode;
        this.delimiter = delimiter;
        this.encode = encode;
    }

    public void transformer() throws IOException {
        CsvReader csvReader = null;
        try {
            csvReader = new CsvReader(file, delimiter, Charset.forName(encode));
            csvReader.readHeaders();
            while (csvReader.readRecord()) {
                List<String> contents = new ArrayList<String>();
                for (Entry<String, PredecodeAction> entry : fieldPredecode.entrySet()) {
                    String content = csvReader.get(entry.getKey());
                    String preDecode = entry.getValue().predecode(content);
                    contents.add(preDecode);
                }
                saveAction.save(contents);
            }
        }
        finally {
            csvReader.close();
        }
    }

}
