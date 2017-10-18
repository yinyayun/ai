package org.yinyayun.ai.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.yinyayun.ai.utils.iface.SaveAction;

public class TokenLinesWriter {

	@Test
	public void executor() throws IOException {
		String[] files = { "C:\\Users\\yinyayun\\Desktop\\xyz咨询数据\\百度标注\\lexical-json.txt", };
		String saveFilePath = "C:/Users/yinyayun/Desktop/xyz咨询数据/分词/token-lines.txt";
		File saveFile = new File(saveFilePath);
		FileUtils.deleteQuietly(saveFile);
		Function<String, String> parserFunction = parserFunction();
		try (BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(saveFilePath), "utf-8"), 1024 * 1024)) {
			SaveAction<String> saveAction = x -> {
				try {
					writer.write(x.concat("\n"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			};
			for (String file : files) {
				BDJsonToText<String> bdJsonToText = new BDJsonToText<String>(file, parserFunction, saveAction);
				bdJsonToText.process();
			}
		}
	}

	public final static Set<String> punctuations = new HashSet<String>(Arrays.asList(
			new String[] { "。", "：", ":", ";", "；", "？", "?", ",", "，", "!", "！", "'", "\"", "’", "“", "”", "\n" }));

	private Function<String, String> parserFunction() {
		return (x) -> {
			StringBuilder builder = new StringBuilder();
			try {
				String del = " ";
				JSONObject jsonObject = new JSONObject(x);
				JSONArray array = jsonObject.getJSONArray("items");
				for (int i = 0; i < array.length(); i++) {
					JSONObject item = array.getJSONObject(i);
					String word = preParser(item.getString("item")).replace(" ", "");
					if (word.length() == 0 || punctuations.contains(word)) {
						continue;
					} else if ("新一".equals(word) && (i + 1) < array.length()) {
						String nextWord = array.getJSONObject(i + 1).getString("item").trim();
						if ("站".equals(nextWord)) {
							builder.append(word).append(nextWord).append(del);
							i++;
						}
					}
					builder.append(word).append(del);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return builder.toString();
		};
	}

	private static String preParser(String word) {
		return word.replace("　", "").trim();
	}
}
