/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.ai.baidu.task;

/**
 * TaskAction.java
 *
 * @author yinyayun
 */
@FunctionalInterface
public interface TaskAction<T, V> {
    public void action(T t, V v);
}
