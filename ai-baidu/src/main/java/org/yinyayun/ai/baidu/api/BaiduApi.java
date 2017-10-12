/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.ai.baidu.api;

import org.yinyayun.ai.utils.proxy.ProxyFactory;

/**
 * BaiduApi.java
 *
 * @author yinyayun
 */
public abstract class BaiduApi {
	protected ProxyFactory proxyFactory;
	protected boolean debug;

	public BaiduApi(ProxyFactory proxyFactory, boolean debug) {
		this.proxyFactory = proxyFactory;
		this.debug = debug;
	}

	public abstract String lexical(String text, int retryTimes);

	public String lexical(String text) {
		return lexical(text, 1);
	}

	public abstract String sentenceParser(String text, int retryTimes);

	public String sentenceParser(String text) {
		return sentenceParser(text, 1);
	}
}
