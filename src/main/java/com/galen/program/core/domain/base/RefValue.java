package com.galen.program.core.domain.base;

import com.galen.program.core.domain.Entity;
import com.galen.program.core.domain.Value;

/**
 * Created by baogen.zhang on 2019/1/4
 *
 * @author baogen.zhang
 * @date 2019/1/4
 */
public class RefValue implements Value {

    private Entity target;

    public RefValue(Entity target) {
        this.target = target;
    }

    public Entity getTarget() {
        return target;
    }

    public Value getValue(){
        return target.getEffectValue();
    }

    @Override
    public <T> T toObject(Class<T> tClass) {
        return getValue().toObject(tClass);
    }

    @Override
    public <T> T[] toArray(Class<T> tClass) {
        return getValue().toArray(tClass);
    }

    @Override
    public Value transform(Object object) {
        return null;
    }
}
