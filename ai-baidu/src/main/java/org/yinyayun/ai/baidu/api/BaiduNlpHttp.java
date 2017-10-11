/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.ai.baidu.api;

import java.io.IOException;

import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.yinyayun.ai.baidu.utils.AppConfig;

/**
 * BaiduAiHttpAPI.java 封装HTTP调用方式
 * 
 * @author yinyayun
 */
public class BaiduNlpHttp extends BaiduApi {
	private final static String ACCESSTOKEN_URL = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=${client_id}&client_secret=${client_secret}";
	private final static String URL = "https://aip.baidubce.com/rpc/2.0/nlp/v1/depparser?access_token=";
	private final static String PARAMS = "{\"text\":\"${text}\"}";
	private String url;
	private Connection connection;

	public BaiduNlpHttp(AppConfig config) {
		try {
			this.url = URL + accessToken(config.apiKey, config.secretKey);
			this.connection = HttpConnection.connect(url).ignoreContentType(true).timeout(10000).postDataCharset("GBK")
					.header("Content-Type", "application/json").method(Method.POST);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 句法分析
	 * 
	 * @param url
	 * @param token
	 * @param text
	 * @return
	 * @throws IOException
	 */
	@Override
	public String sentenceParser(String text, int retryTime) {
		Response response;
		try {
			response = connection.requestBody(PARAMS.replace("${text}", text)).execute();
			String json = new String(response.bodyAsBytes(), "GBK");
			JSONObject jsonObject = new JSONObject(json);
			if (jsonObject.has("error")) {
				return null;
			} else {
				return json;
			}
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public String lexical(String text, int retryTimes) {
		return null;
	}

	/**
	 * 获取临时token key
	 * 
	 * @param properties
	 * @return
	 * @throws IOException
	 */
	private String accessToken(String apiKey, String secretKey) throws IOException {
		String accessUrl = accessUrl(apiKey, secretKey);
		Connection accessConn = HttpConnection.connect(accessUrl);
		Document doc = accessConn.ignoreContentType(true).get();
		JSONObject jsonObject = new JSONObject(doc.text());
		if (jsonObject.has("error")) {
			throw new RuntimeException(
					"can't get access token key,message is " + jsonObject.getString("error_description"));
		} else {
			System.out.println(jsonObject.getString("access_token"));
			return jsonObject.getString("access_token");
		}
	}

	/**
	 * token key获取url
	 * 
	 * @param aipKey
	 * @param secretKey
	 * @return
	 */
	private String accessUrl(String aipKey, String secretKey) {
		return ACCESSTOKEN_URL.replace("${client_id}", aipKey).replace("${client_secret}", secretKey);
	}
}
