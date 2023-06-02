package com.ywl.framework.web.translate;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ywl.framework.common.cache.LocalCache;
import com.ywl.framework.common.model.IDict;
import com.ywl.framework.common.utils.ReflectUtil;
import com.ywl.framework.common.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Aspect
@Component
@Order(2)
@Slf4j
public class TranslatorAspect {


    @Resource
    private TranslateConvertFactory translateConvertFactory;

    @Pointcut("@annotation(com.ywl.framework.web.translate.Translator)")
    public void pointCut() {
    }


    @AfterReturning(pointcut = "pointCut()", returning = "object")
    @SuppressWarnings("unchecked")
    public void doAfter(JoinPoint joinPoint, Object object) {
        TranslateTypeConvert<Object> translateTypeConvert = translateConvertFactory.getTranslateTypeConvert((Class<Object>) object.getClass());
        Object result;
        if (translateTypeConvert != null) {
            result = translateTypeConvert.convert(object);
        } else {
            result = object;
        }
        if (result instanceof Collection<?> || result instanceof IPage) {
            Collection<?> collection = result instanceof IPage ? ((IPage<?>) result).getRecords() : (Collection<?>) result;
            if (collection.isEmpty()) {
                return;
            }
            collection.forEach((Consumer<Object>) this::translateObject);
            log.info("返回列表结果 {}", collection);
        } else {
            this.translateObject(object);
            log.info("返回对象结果 {}", object);
        }
    }

    private void translateObject(Object result) {
        if (Objects.isNull(result)) {
            return;
        }
        Field[] fields = ReflectUtil.getFields(result.getClass());
        // 翻译当前对象字段
        Arrays.stream(fields).filter(field -> field.getAnnotation(Translate.class) != null)
                .forEach(field -> translateField(result, field));
        // 翻译内部属性字段
        Arrays.stream(fields).filter(field -> field.getAnnotation(TranslateField.class) != null)
                .forEach(field -> {
                    try {
                        Object subObject = ReflectUtil.invokeGet(result, field);
                        if (subObject instanceof Collection<?>) {
                            ((Collection<?>) subObject).forEach(this::translateObject);
                        } else {
                            this.translateObject(subObject);
                        }
                    } catch (Exception e) {
                        log.error("翻译错误", e);
                    }
                });
    }

    private List<Field> filterTranslate(Class<?> voType) {
        Field[] fields = voType.getDeclaredFields();
        return Arrays.stream(fields)
                .filter(field -> field.getAnnotation(Translate.class) != null).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private void translateField(Object vo, Field field) {
        try {
            Translate annotation = field.getAnnotation(Translate.class);
            Class<?> aClass = annotation.dataSource();
            Object fieldValue = ReflectUtil.invokeGet(vo, annotation.from());
            if (Objects.isNull(fieldValue)) {
                return;
            }
            if (IDict.class.isAssignableFrom(aClass)) {
                String translate = new EnumTranslatable().translate(fieldValue, aClass, null);
                if (translate == null) {
                    return;
                }
                ReflectUtil.invokeSet(vo, field, translate);
            } else if (LocalCache.class.isAssignableFrom(annotation.dataSource())) {
                LocalCacheTranslator<Object, Object> localCacheTranslator = SpringContextUtil.getBean(LocalCacheTranslator.class);
                String translate = localCacheTranslator.translate(fieldValue, annotation.dataSource(), annotation.param());
                if (translate == null) {
                    return;
                }
                ReflectUtil.invokeSet(vo, field, translate);
            } else {
                Translatable<Object> translatable = SpringContextUtil.getBean(annotation.translator());
                String translate = translatable.translate(fieldValue, annotation.dataSource(), annotation.param());
                if (translate == null) {
                    return;
                }
                ReflectUtil.invokeSet(vo, field, translate);
            }
        } catch (Exception e) {
            log.error("翻译错误", e);
        }
    }

}
