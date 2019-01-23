package com.galen.program.core.domain;

/**
 * Created by baogen.zhang on 2019/1/4
 *
 * @author baogen.zhang
 * @date 2019/1/4
 */
public interface Value {

    /**
     * 将对象转换成目标类型，并返回。
     *
     * @param tClass 目标类型  List 类型需传泛型类型
     * @return 目标类型对象
     */
    <T> T toObject(Class<T> tClass);

    <T> T[] toArray(Class<T> tClass);

    Value transform(Object object);
}
