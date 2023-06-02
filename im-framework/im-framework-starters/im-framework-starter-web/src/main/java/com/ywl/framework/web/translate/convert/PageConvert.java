package com.ywl.framework.web.translate.convert;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ywl.framework.web.translate.AbstractTranslateConvertRegistry;
import com.ywl.framework.web.translate.TranslateTypeConvert;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PageConvert {

    public static class ResultWrapperTranslateTypeConvert implements TranslateTypeConvert<IPage> {
        @Override
        public Object convert(IPage object) {
            return object.getRecords();
        }
    }

    @Bean
    public AbstractTranslateConvertRegistry initTranslateConvertRegistry() {
        return new AbstractTranslateConvertRegistry() {

            @Override
            public TranslateTypeConvert addTranslateTypeConvert() {
                return new ResultWrapperTranslateTypeConvert();
            }
        };
    }
}
