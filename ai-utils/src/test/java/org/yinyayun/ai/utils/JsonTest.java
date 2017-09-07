/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.ai.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

/**
 * JsonTest.java
 *
 * @author yinyayun
 */
public class JsonTest {
    @Test
    public void json() {
        try {
            String json = FileUtils.readFileToString(new File("data/error-example.txt"), "UTF-8");
            JSONObject jsonObject = new JSONObject(json);
            JSONArray array = jsonObject.getJSONArray("items");
            for (int i = 0; i < array.length(); i++) {
                System.out.println(array.getJSONObject(i).getString("item"));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
