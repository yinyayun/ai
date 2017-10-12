package org.yinyayun.ai.baidu.api;

import org.json.JSONObject;
import org.yinyayun.ai.baidu.utils.AppConfig;
import org.yinyayun.ai.utils.proxy.ProxyFactory;

import com.baidu.aip.nlp.AipNlp;

/**
 * 百度NLP接口
 * 
 * @author yinyayun
 */
public class BaiduNlpSDK extends BaiduApi {

	private AipNlp client;

	public BaiduNlpSDK(AppConfig config, ProxyFactory proxyFactory) {
		this(config, proxyFactory, false);
	}

	public BaiduNlpSDK(AppConfig config, ProxyFactory proxyFactory, boolean debug) {
		super(proxyFactory, debug);
		this.client = new AipNlp(config.appid, config.apiKey, config.secretKey);
		client.setConnectionTimeoutInMillis(config.connTimeout);
		client.setSocketTimeoutInMillis(config.socketTimeout);
	}

	/**
	 * 词法分析
	 * 
	 * @param text
	 *            长度不能超过16KB
	 * @param retryTimes
	 * @return null:如果无数据返回，或者调用出错
	 */
	@Override
	public String lexical(String text, int retryTimes) {
		for (int i = 0; i < retryTimes; i++) {
			JSONObject object = client.lexer(text);
			if (object.has("error_code")) {
				if (i != (retryTimes - 1)) {
					continue;
				}
			} else {
				return object.toString();
			}
		}
		return null;
	}

	/**
	 * 句法分析接口
	 * 
	 * @return
	 */
	@Override
	public String sentenceParser(String text, int retryTimes) {
		for (int i = 0; i < retryTimes; i++) {
			JSONObject object = client.depParser(text, null);
			if (object.has("error_code")) {
				if (i != (retryTimes - 1)) {
					continue;
				}
			} else {
				return object.toString();
			}
		}
		return null;
	}
}
