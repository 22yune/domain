package com.galen.program.core.domain.base;

import com.galen.program.core.domain.Value;

/**
 * Created by baogen.zhang on 2019/1/4
 *
 * @author baogen.zhang
 * @date 2019/1/4
 */
public class BaseValue implements Value {
    @Override
    public <T> T toObject(Class<T> tClass) {
        return null;
    }

    @Override
    public <T> T[] toArray(Class<T> tClass) {
        return null;
    }

    @Override
    public Value transform(Object object) {
        return null;
    }
}
