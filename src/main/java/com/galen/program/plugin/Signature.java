package com.galen.program.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by baogen.zhang on 2018/11/26
 *
 * @author baogen.zhang
 * @date 2018/11/26
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Signature {
    Class<?> type();

    String method();

    Class<?>[] args();
}