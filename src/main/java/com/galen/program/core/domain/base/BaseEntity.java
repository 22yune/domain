package com.galen.program.core.domain.base;

import com.galen.program.core.domain.Entity;
import com.galen.program.core.domain.Identity;
import com.galen.program.core.domain.State;
import com.galen.program.core.domain.Value;

/**
 * Created by baogen.zhang on 2019/1/4
 *
 * @author baogen.zhang
 * @date 2019/1/4
 */
public class BaseEntity implements Entity {
    private Identity identity;
    private State state;
    private Value value;

    @Override
    public Entity setIdentity(Identity identity) {
        this.identity = identity;
        return this;
    }

    @Override
    public Identity getIdentity() {
        return identity;
    }

    @Override
    public Entity setState(State state) {
        this.state = state;
        return this;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public Entity setValue(Value value) {
        this.value = value;
        return this;
    }

    @Override
    public Value getValue() {
        return value;
    }

    @Override
    public Value getEffectValue() {
        return value;
    }

    @Override
    public Entity construct(Object... objects) {
        return this;
    }

    @Override
    public void destruct() {

    }
}
