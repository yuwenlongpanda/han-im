package com.ywl.framework.web.translate;

import org.springframework.context.SmartLifecycle;

import javax.annotation.Resource;

public abstract class AbstractTranslateConvertRegistry implements SmartLifecycle {

    @Resource
    private TranslateConvertFactory translateConvertFactory;

    public abstract TranslateTypeConvert addTranslateTypeConvert();

    @Override
    public void start() {
        translateConvertFactory.addConvert(addTranslateTypeConvert());
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
