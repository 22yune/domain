package com.galen.program.space;

import java.util.Set;

/**
 * Created by baogen.zhang on 2018/11/24
 *
 * @author baogen.zhang
 * @date 2018/11/24
 */
public interface Space<T, IN extends Space<T, ?, ?, ?, ?>, OUT extends Space<T, ?, ?, ?, ?>, DIN, DOUT> {

    /**
     * 设置空间名称
     *
     * @param name
     */
    void setName(String name);

    /**
     * 空间名称
     */
    String getName();

    /**
     * 是否是命名的空间，如果是，可以通过空间名称定位到空间；如果否，空间只是外部空间的一个区域。
     *
     * @return
     */
    boolean isNameSpace();

    /**
     * 表示容器是否已就绪:有未定义的对象就返回否
     */
    boolean isReady();

    /**
     * 添加对象，对象必须是通过该{@link Space#getObject(String)}获取的，否则添加时，如果已有相同名称的对象会抛出异常。
     * 使用规范：
     * 不论是定义还是访问对象，都应该先定位到对象所属的{@link Space}，调用{@link Space#getObject(String)}方法获取对象。
     * 在定义对象时，都应该先定位到对象所属的{@link Space}，调用{@link Space#addObject(String, Object)}方法添加对象。重复定义对象会报错。
     *
     * @param name
     * @param object
     */
    void addObject(String name, T object);

    /**
     * 返回暴露出的对象，如果对象还没定义，就先返回对象引用。
     *
     * @param name
     * @return
     * @see Space#addObject(String, Object)
     */
    T getObject(String name);

    /**
     * 返回所有已暴露出的对象名称。
     *
     * @return
     */
    Set<String> getObjectNames();

    /**
     * 添加子空间，子空间必须是通过该{@link Space#getSpace(String)}获取的，否则添加时，如果已有相同名称的子空间会抛出异常。
     * 使用规范：
     * 不论是定义还是访问子空间，都应该先定位到空间所属的{@link Space}，调用{@link Space#getSpace(String)} 方法获取空间。
     * 在定义空间时，都应该先定位到空间所属的{@link Space}，调用{@link Space#addSpace(String,Space)}方法添加空间。重复定义空间会报错。
     *
     * @param name
     * @param space
     * @return
     */
    void addSpace(String name, IN space);

    /**
     * 返回暴露出的子空间，如果子空间还未定义，就先返回空间对象引用。
     *
     * @param name
     * @return
     * @see Space#addSpace(String, Space)
     */
    IN getSpace(String name);

    /**
     * 返回所有已暴露出的子空间名称。
     *
     * @return
     */
    Set<String> getSpaceNames();

    /**
     * 设置外部空间
     *
     * @param space
     */
    void setOutSpace(OUT space);

    /**
     * 获取外部空间
     *
     * @return
     */
    OUT getOutSpace();

    /**
     * 返回根空间
     *
     * @return
     */
    Space<T, ?, ?, ?, ?> rootSpace();

    /**
     * 是否包含指定空间
     *
     * @param space
     * @return 指定空间是本空间或本空间的子空间返回是，否则返回否
     */
    boolean isInclude(Space<T, ?, ?, ?, ?> space);

    /**
     * 根据输入数据构建空间
     *
     * @param dataIn
     * @return
     */
    void build(DIN dataIn);//TODO 构建生命周期管理

}
