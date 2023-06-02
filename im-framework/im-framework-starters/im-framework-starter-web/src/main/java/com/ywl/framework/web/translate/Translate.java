package com.ywl.framework.web.translate;

import java.lang.annotation.*;

/**
 * @author liao
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.FIELD)
public @interface Translate {

    Class<? extends Translatable> translator() default Translatable.class;

    Class<?> dataSource() default Void.class;

    String from();

    String param() default "";

}
