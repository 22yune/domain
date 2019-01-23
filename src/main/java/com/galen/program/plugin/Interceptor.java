package com.galen.program.plugin;

/**
 * Created by baogen.zhang on 2018/11/26
 *
 * @author baogen.zhang
 * @date 2018/11/26
 */
public interface Interceptor {


    Object intercept(Invocation invocation) throws Throwable;

    Object plugin(Object target);

}
