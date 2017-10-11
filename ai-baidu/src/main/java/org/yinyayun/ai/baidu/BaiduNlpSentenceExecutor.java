package org.yinyayun.ai.baidu;

import org.yinyayun.ai.baidu.api.BaiduApi;
import org.yinyayun.ai.baidu.api.BaiduNlpHttp;
import org.yinyayun.ai.baidu.task.TextEntity;
import org.yinyayun.ai.baidu.utils.AppConfig;

/**
 * 句法分析数据抓取
 * 
 * @author yinyayun
 *
 */
public class BaiduNlpSentenceExecutor extends BaiduNlpExecutor {
	public static void main(String[] args) {
		String dataFile = "C:/Users/yinyayun/Desktop/xyz咨询数据/句子/corpus-sentences.txt";
		String saveFile = "C:/Users/yinyayun/Desktop/xyz咨询数据/句子/corpus-sentences-json.txt";
		String completeFile = "C:/Users/yinyayun/Desktop/xyz咨询数据/句子/completes.txt";
		new BaiduNlpSentenceExecutor().executor(dataFile, saveFile, completeFile);
	}

	@Override
	public String doAction(TextEntity entity, BaiduApi api) {
		return api.sentenceParser(entity.text);
	}

	@Override
	public BaiduApi buildApi(AppConfig config) {
		return new BaiduNlpHttp(config);
	}

}
