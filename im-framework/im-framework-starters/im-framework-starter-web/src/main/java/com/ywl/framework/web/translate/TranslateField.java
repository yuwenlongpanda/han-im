package com.ywl.framework.web.translate;

import java.lang.annotation.*;

/**
 * 增加该注解，表示此字段内部需要进行翻译
 *
 * @author peng.xy
 * @return
 * @date 2022/3/31
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.FIELD)
public @interface TranslateField {

}
