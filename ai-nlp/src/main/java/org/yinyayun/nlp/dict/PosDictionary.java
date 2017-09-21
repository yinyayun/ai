/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.nlp.dict;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Set;

import org.json.JSONObject;

import opennlp.tools.postag.TagDictionary;

/**
 * PosDictionary.java
 *
 * @author yinyayun
 */
public class PosDictionary implements TagDictionary {

    private final String dictName = "pos-dictory.json";
    private JSONObject dict;

    public void loadDict() throws IOException {
        File tmpFile = File.createTempFile("nlp", dictName);
        try (InputStream in = this.getClass().getClassLoader().getResourceAsStream(dictName)) {
            Files.copy(in, Paths.get(tmpFile.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
            dict = new JSONObject(new String(Files.readAllBytes(tmpFile.toPath())));
        }
        finally {
            tmpFile.deleteOnExit();
        }
    }

    @Override
    public String[] getTags(String word) {
        String[] tags = null;
        if (dict != null && dict.has(word)) {
            JSONObject tagJson = dict.getJSONObject(word);
            Set<String> keys = tagJson.keySet();
            tags = new String[keys.size()];
            int i = 0;
            for (String key : keys) {
                tags[i++] = key;
            }
        }
        return tags;
    }

    public static void main(String[] args) throws IOException {
        PosDictionary dictionary = new PosDictionary();
        dictionary.loadDict();
        System.out.println(Arrays.deepToString(dictionary.getTags("张伟")));
    }
}
