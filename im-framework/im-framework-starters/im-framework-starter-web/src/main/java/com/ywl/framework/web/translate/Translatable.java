package com.ywl.framework.web.translate;

/**
 * @author liao
 */
public interface Translatable<T> {

    /**
     * 翻译
     * @param original
     * @param datasource
     * @param param
     * @return
     */
    String translate(T original, Class<?> datasource, String param);
}
