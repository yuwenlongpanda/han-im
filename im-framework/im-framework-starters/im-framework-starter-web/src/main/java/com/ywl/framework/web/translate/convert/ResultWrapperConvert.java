package com.ywl.framework.web.translate.convert;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ywl.framework.web.result.ResultWrapper;
import com.ywl.framework.web.translate.AbstractTranslateConvertRegistry;
import com.ywl.framework.web.translate.TranslateTypeConvert;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResultWrapperConvert {

    public static class ResultWrapperTranslateTypeConvert implements TranslateTypeConvert<ResultWrapper> {

        @Override
        public Object convert(ResultWrapper object) {
            Object data = object.getData();
            //如果是page则拿到data 再返回
            if (data instanceof Page) {
                return ((Page<?>) data).getRecords();
            }
            return data;
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
