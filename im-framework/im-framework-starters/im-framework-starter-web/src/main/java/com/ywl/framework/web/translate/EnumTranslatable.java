package com.ywl.framework.web.translate;

import com.ywl.framework.common.model.IDict;

import java.util.stream.Stream;

public class EnumTranslatable implements Translatable<Object> {

    @Override
    @SuppressWarnings("unchecked")
    public String translate(Object original, Class<?> datasource, String param) {
        Class<IDict<?>> iDict = (Class<IDict<?>>) datasource;
        return Stream.of(iDict.getEnumConstants())
                .filter(i -> i.getCode().equals(original)).map(IDict::getText).findFirst().orElse(null);
    }
}
