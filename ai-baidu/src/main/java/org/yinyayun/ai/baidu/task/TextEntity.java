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
    private String id;
    private String text;

    public TextEntity(String id, String text) {
        super();
        this.id = id;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
