package org.yinyayun.ai.baidu;

import org.yinyayun.ai.baidu.api.BaiduApi;
import org.yinyayun.ai.baidu.api.BaiduNlpHttp;
import org.yinyayun.ai.baidu.task.TextEntity;
import org.yinyayun.ai.baidu.utils.AppConfig;
import org.yinyayun.ai.utils.proxy.ProxyFactory;

/**
 * 句法分析数据抓取
 * 
 * @author yinyayun
 *
 */
public class BaiduNlpSentenceExecutor extends BaiduNlpExecutor {
	public static void main(String[] args) {
		String dataFile = "D:/Data/xyz咨询数据/句子/corpus-sentences.txt";
		String saveFile = "D:/Data/xyz咨询数据/句子/corpus-sentences-json.txt";
		String completeFile = "D:/Data/xyz咨询数据/句子/completes.txt";
		// ProxyFactory proxyFactory = new ProxyFactory(new ProxyCrawler());
		new BaiduNlpSentenceExecutor().executor(2, dataFile, saveFile, completeFile, 1, null);
	}

	@Override
	public String doAction(TextEntity entity, BaiduApi api) {
		return api.sentenceParser(entity.text);
	}

	@Override
	public BaiduApi buildApi(AppConfig config, ProxyFactory proxyFactory) {
		return new BaiduNlpHttp(config, proxyFactory, false);
	}

}
