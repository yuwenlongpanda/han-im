package com.ywl.framework.web.translate;



@FunctionalInterface
public interface TranslateTypeConvert<T> {

    Object convert(T object);
}
