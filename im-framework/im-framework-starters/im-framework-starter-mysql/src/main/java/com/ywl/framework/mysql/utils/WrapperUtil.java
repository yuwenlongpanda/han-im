package com.ywl.framework.mysql.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.lang.NonNull;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

public class WrapperUtil {

    /**
     * VO直接转成QueryWrapper
     *
     * @param anyVO vo对象
     * @return LambdaQueryWrapper
     */
    public static <T> LambdaQueryWrapper<T> parse(Object anyVO, Class<T> clazz) throws Throwable {
        InnerLambdaQueryWrapper<T> wrapper = new InnerLambdaQueryWrapper<>();
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        PropertyDescriptor[] propertyDescriptors = ReflectUtils.getBeanGetters(anyVO.getClass());
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            Method getMethod = propertyDescriptor.getReadMethod();
            CallSite apply = LambdaMetafactory.altMetafactory(lookup, "apply",
                    MethodType.methodType(SFunction.class),
                    MethodType.methodType(Object.class, Object.class),
                    lookup.unreflect(getMethod),
                    MethodType.methodType(Object.class, clazz),
                    LambdaMetafactory.FLAG_SERIALIZABLE,
                    Serializable.class
            );
            SFunction<T, ?> getFunction = (SFunction<T, ?>) apply.getTarget().invokeExact();
            Object value = getMethod.invoke(anyVO);
            if (value == null) {
                continue;
            }
            Pair<SqlKeyword, Object> pair = parseValue(value);
            wrapper.addCondition(true, getFunction, pair.getKey(), pair.getValue());
            return wrapper;
        }
        System.out.println(wrapper.getCustomSqlSegment());
        return null;
    }

    private static Pair<SqlKeyword, Object> parseValue(@NonNull Object value) {
        if (!(value instanceof String)) {
            return Pair.of(SqlKeyword.EQ, value);
        }
        String valueStr = (String) value;
        String type = valueStr.substring(0, valueStr.indexOf(":") + 1);
        return null;
    }

    public static void main(String[] args) throws Throwable {
        parse(new ProductCategoryVO().setName("efwef"), ProductCategoryVO.class);
    }

    private static class InnerLambdaQueryWrapper<T> extends LambdaQueryWrapper<T> {
        @Override
        public LambdaQueryWrapper<T> addCondition(boolean condition, SFunction<T, ?> column, SqlKeyword sqlKeyword, Object val) {
            return super.addCondition(condition, column, sqlKeyword, val);
        }
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    public static class ProductCategoryVO implements Serializable {

        @ApiModelProperty("商品分类id")
        private Long id;

        @ApiModelProperty("父分类id")
        private Long parentId;

        @ApiModelProperty("分类名称")
        private String name;
    }
}
