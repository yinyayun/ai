/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.ai.baidu.api;

/**
 * BaiduApi.java
 *
 * @author yinyayun
 */
public abstract class BaiduApi {
    public abstract String lexical(String text, int retryTimes);

    public String lexical(String text) {
        return lexical(text, 3);
    }

    public abstract String sentenceParser(String text, int retryTimes);

    public String sentenceParser(String text) {
        return sentenceParser(text, 3);
    }
}
