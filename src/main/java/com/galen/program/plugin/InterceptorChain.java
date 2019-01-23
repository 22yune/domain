package com.galen.program.plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baogen.zhang on 2018/11/26
 *
 * @author baogen.zhang
 * @date 2018/11/26
 */
public class InterceptorChain {

    private final List<Interceptor> interceptors = new ArrayList<Interceptor>();

    public Object pluginAll(Object target) {
        for (Interceptor interceptor : interceptors) {
            target = interceptor.plugin(target);
        }
        return target;
    }

    public void addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
    }
}
