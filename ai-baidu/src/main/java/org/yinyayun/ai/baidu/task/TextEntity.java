/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.ai.baidu.task;

/**
 * TextEntity.java
 *
 * @author yinyayun
 */
public class TextEntity {
	public int id;
	public String text;
	public String response;

	public TextEntity(int id, String text) {
		super();
		this.id = id;
		this.text = text;
	}

	public void setResponse(String response) {
		this.response = response;
	}

}
