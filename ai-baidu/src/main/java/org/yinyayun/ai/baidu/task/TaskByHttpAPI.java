/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.ai.baidu.task;

import java.io.IOException;
import java.util.Map;

import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.yinyayun.ai.baidu.BaiduNlpAnalysis;
import org.yinyayun.ai.baidu.ClientFactory;
import org.yinyayun.ai.baidu.utils.PropertiesUtil;
import org.yinyayun.netcarry.core.ConnectionFactory;

/**
 * TaskByHttpApi.java
 *
 * @author yinyayun
 */
public class TaskByHttpAPI implements Runnable {
	private final static String ACCESSTOKEN_URL = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=${client_id}&client_secret=${client_secret}";
	private final static String URL = "https://aip.baidubce.com/rpc/2.0/nlp/v1/depparser?access_token=${access_token}";
	private final static String PARAMS = "{\"text\":\"${text}\"}";
	private String url;
	private TaskQueue queue;
	private TaskQueue saveQueue;
	private BaiduNlpAnalysis analysis;
	private ConnectionFactory connectionFactory;

	public static void main(String[] args) throws IOException {
		Map<String, Map<String, String>> groups = PropertiesUtil.groupSuffixconfigKeys(
				new String[] { ClientFactory.APP_ID, ClientFactory.API_KEY, ClientFactory.SECRET_KEY });
		TaskByHttpAPI api = new TaskByHttpAPI(null, null, null, null, null);
		// String token = api.accessToken(groups.get("9."));
		String token = "24.0724c2d524cbec1080391940e0c14028.2592000.1509353565.282335-10204070";
		api.sentenceParser(token, "我爱自然语言");
	}

	public TaskByHttpAPI(TaskQueue queue, TaskQueue saveQueue, BaiduNlpAnalysis analysis,
			ConnectionFactory connectionFactory, String url) {
		this.queue = queue;
		this.saveQueue = saveQueue;
		this.analysis = analysis;
		this.url = url;
		this.connectionFactory = connectionFactory;
	}

	@Override
	public void run() {
		while (true) {
			TextEntity textEntity = queue.take();
			Connection connection = connectionFactory.createConnection(url);
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
	private String sentenceParser(String token, String text) throws IOException {
		Connection connection = HttpConnection.connect(url.replace("${access_token}", token));
		Response response = connection.ignoreContentType(true).postDataCharset("GBK")
				.header("Content-Type", "application/json").requestBody(PARAMS.replace("${text}", text))
				.method(Method.POST).execute();
		String json = new String(response.bodyAsBytes(), "GBK");
		JSONObject jsonObject = new JSONObject(json);
		if (jsonObject.has("error")) {
			return null;
		} else {
			return json;
		}
	}

	/**
	 * 获取临时token key
	 * 
	 * @param properties
	 * @return
	 * @throws IOException
	 */
	private String accessToken(Map<String, String> properties) throws IOException {
		String accessUrl = accessUrl(properties.get(ClientFactory.API_KEY), properties.get(ClientFactory.SECRET_KEY));
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

	private String url(String accessToken) {
		return URL.replace("${access_token}", accessToken);
	}
}
