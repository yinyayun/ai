/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.ai.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * TxtFileReader.java
 *
 * @author yinyayun
 */
public class TxtFileReader implements AutoCloseable {
    private BufferedReader reader;
    private String line;

    public TxtFileReader(String path) {
        try {
            this.reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), Charset.forName("utf-8")),
                    1024 * 1024);
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public boolean hasNext() throws IOException {
        this.line = reader.readLine();
        if (line == null) {
            return false;
        }
        else {
            return true;
        }
    }

    public String readLine() {
        return line;
    }

    @Override
    public void close() throws Exception {

    }

}
