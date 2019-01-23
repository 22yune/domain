package com.galen.program.core.domain;


/**
 * 实体对象
 * 有身份标示：身份标示唯一且不可变。
 * 有生命周期：
 * <p>
 * Created by baogen.zhang on 2019/1/4
 *
 * @author baogen.zhang
 * @date 2019/1/4
 */
public interface Entity {

    /**
     * 设置身份标示，只能设置一次，设置后就不能改变。
     *
     * @param identity
     */
    Entity setIdentity(Identity identity);

    /**
     * 获取身份标示。
     *
     * @return
     */
    Identity getIdentity();

    /**
     * 设置状态
     *
     * @param state
     * @return
     */
    Entity setState(State state);

    /**
     * 获取状态
     *
     * @return
     */
    State getState();

    /**
     * 设置值对象
     *
     * @param value
     * @return
     */
    Entity setValue(Value value);

    /**
     * 获取具体内容
     *
     * @return
     */
    Value getValue();

    /**
     * 获取整体生效的内容
     *
     * @return
     */
    Value getEffectValue();

    Entity construct(Object... objects);

    void destruct();


}
