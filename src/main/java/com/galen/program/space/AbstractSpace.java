package com.galen.program.space;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;


/**
 * Created by baogen.zhang on 2018/11/24
 *
 * @author baogen.zhang
 * @date 2018/11/24
 */
public abstract class AbstractSpace<T, IN extends Space<T, ?, ?, ?, ?>, OUT extends Space<T, ?, ?, ?, ?>, DIN, DOUT> implements Space<T, IN, OUT, DIN, DOUT> {
    private static Logger logger = LoggerFactory.getLogger(AbstractSpace.class);

    private String name;
    private Map<String, T> definedObjects = new HashMap<String, T>();
    private Map<String, T> undefinedObjects = new HashMap<String, T>();
    private Map<String, IN> definedSpaces = new HashMap<String, IN>();
    private Map<String, IN> undefinedSpaces = new HashMap<String, IN>();
    private OUT outSpace;

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isNameSpace() {
        return true;
    }

    @Override
    public boolean isReady() {
        if (!undefinedObjects.isEmpty() || !undefinedSpaces.isEmpty()) {
            return false;
        }
        for (Space space : definedSpaces.values()) {
            if (!space.isReady()) {
                return false;
            }
        }
        return true;
    }

    protected void valid() {
        if (!isReady()) {
            StringBuilder stringBuilder = new StringBuilder("未定义对象！");
            for (Map.Entry<String, T> t : undefinedObjects.entrySet()) {
                stringBuilder.append(t.toString());
                stringBuilder.append(",\n");
            }
            throw new IllegalStateException("定义不完整！" + stringBuilder);
        }
    }

    @Override
    public void addObject(String name, T object) {
        Address address = parseObjectName(name);
        addObject(address, object);
    }

    protected void addObject(Address address, T object) {
        Space<T, ?, ?, ?, ?> space = routeSpace(address);
        if (!space.equals(this)) {
            space.addObject(address.toString(), object);
            return;
        }
        String objectName = address.getObject().getName();
        doAddObject(objectName, object);
    }

    protected void doAddObject(String objectName, T object) {
        T exists = undefinedObjects.get(objectName);
        if (exists != null && !exists.equals(object)) {
            throw new UnableToRegisterException(objectName + "代码异常！添加的不是初始对象！");
        }
        exists = definedObjects.get(objectName);
        if (exists != null && !exists.equals(object)) {
            throw new UnableToRegisterException(objectName + "代码异常！重复添加对象，不能覆盖！");
        }
        undefinedObjects.remove(objectName);
        definedObjects.put(objectName, object);
    }

    @Override
    public T getObject(String name) {
        Address address = parseObjectName(name);
        return getObject(address);
    }

    protected T getObject(Address address) {
        Space<T, ?, ?, ?, ?> space = routeSpace(address);
        if (!space.equals(this)) {
            return space.getObject(address.toString());
        }
        String objectName = address.getObject().getName();
        return doGetObject(objectName);
    }

    private T doGetObject(String objectName) {
        T exists = definedObjects.get(objectName);
        if (exists == null) {
            exists = undefinedObjects.get(objectName);
        }
        if (exists != null) {
            return exists;
        } else {
            T newObject = createObject(objectName);
            undefinedObjects.put(objectName, newObject);
            return newObject;
        }
    }

    @Override
    public Set<String> getObjectNames() {
        Set<String> names = definedObjects.keySet();
        names.addAll(undefinedObjects.keySet());
        return names;
    }

    @Override
    public void addSpace(String name, IN space) {
        IN exists = undefinedSpaces.get(name);
        if (exists != null && !exists.equals(space)) {
            throw new UnableToRegisterException(name + "代码异常！添加的不是初始对象！");
        }
        exists = definedSpaces.get(name);
        if (exists != null && !exists.equals(space)) {
            throw new UnableToRegisterException(name + "代码异常！重复添加对象，不能覆盖！");
        }
        undefinedSpaces.remove(name);
        definedSpaces.put(name, space);
    }

    @Override
    public IN getSpace(String name) {
        IN exists = definedSpaces.get(name);
        if (exists == null) {
            exists = undefinedSpaces.get(name);
        }
        if (exists != null) {
            return exists;
        } else {
            IN newSpace = createSpace(name);
            undefinedSpaces.put(name, newSpace);
            return newSpace;
        }
    }

    @Override
    public Set<String> getSpaceNames() {
        Set<String> names = definedSpaces.keySet();
        names.addAll(undefinedSpaces.keySet());
        return names;
    }

    @Override
    public void setOutSpace(OUT space) {
        this.outSpace = space;
    }

    @Override
    public OUT getOutSpace() {
        return outSpace;
    }

    @Override
    public Space<T, ?, ?, ?, ?> rootSpace() {
        Space<T, ?, ?, ?, ?> root = getOutSpace();
        while (root != null && root.getOutSpace() != null) {
            root = root.getOutSpace();
        }
        return root;
    }


    /**
     * 根据名称路由到空间
     * 只接受对象名称，否则会报格式不对异常。
     * 空间定位 根据解析后的含路径的对象名称，定义所属空间。
     * 根空间路径 从根空间往下找
     * 本地空间路径 如果是全局对象，从本空间开始往外找第一个命名空间的。如果是局部对象，直接返回本空间。
     *
     * @param address 地址
     * @return 空间
     * @see #parseObjectName(String)
     */
    protected Space<T, ?, ?, ?, ?> routeSpace(Address address) {
        if (address == null) {
            throw new NameParseException("对象名称不能为空！");
        }
        if (address.getObject() == null) {
            throw new NameParseException(address + "不是对象名称！");
        }

        Space space;
        if (address.isSpace()) {
            space = rootSpace();
            while (address != null && address.isSpace() && space != null) {
                space = space.getSpace(address.getName());
                if (space == null || !space.isNameSpace()) {
                    space = null;
                }
                address = address.getNext();
            }
        } else if (address instanceof AddressParser.DataAddress || address instanceof AddressParser.TestAddress) {
            space = this;
            while (space != null && !space.isNameSpace()) {
                space = space.getOutSpace();
            }
        } else {
            space = this;
        }
        if (space == null) {
            throw new DefineFormatException(address + "格式错误，找不到对应空间！");
        }
        return space;
    }

    @Override
    public boolean isInclude(Space<T, ?, ?, ?, ?> space) {
        if (this.equals(space)) {
            return true;
        }
        for (IN in : definedSpaces.values()) {
            if (in.isInclude(space)) {
                return true;
            }
        }
        for (IN in : undefinedSpaces.values()) {
            if (in.isInclude(space)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据输入数据构建空间
     *
     * @param dataIn 定义数据
     */
    @Override
    public void build(DIN dataIn) {
        Collection<DOUT> collection = parse(dataIn);
        if (collection != null) {
            fill(collection);
        }
        valid();
    }

    /**
     * 创建对象
     *
     * @param name 简单对象名称
     * @return 新对象
     */
    protected abstract T createObject(String name);/* {
        return (T) createInstance(1);
    }*/

    /**
     * 创建空间
     *
     * @param name 简单空间名称
     * @return 新空间
     */
    protected abstract IN createSpace(String name);/*{
        IN in = (IN) createInstance(2);
        in.setName(name);
        return in;
    }*/

    protected Object createInstance(int parameterizedTypeIndex) {
        Object object = null;
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        if (actualTypeArguments.length > parameterizedTypeIndex) {
            try {
                object = ((Class) actualTypeArguments[parameterizedTypeIndex]).newInstance();
            } catch (InstantiationException e) {
                logger.error("对象反射创建失败", e);
            } catch (IllegalAccessException e) {
                logger.error("对象反射创建失败", e);
            }
        }
        return object;
    }

    /**
     * 对象名称解析
     * 解析失败报错，不能返回空。
     * <p>
     * 名称格式：
     * {@value AddressParser#CHAR_DATA} 或 {@value AddressParser#CHAR_TEST} 开头的名称表示当前空间的对象。这两个特殊字符只能放在开头，其他位置需通过复写转义，否则会报错。
     * {@value AddressParser#CHAR_LOCATE} 开头的名称表示从根空间一直向下查找的空间。如 {@value AddressParser#CHAR_LOCATE}SpaceA{@value AddressParser#CHAR_LOCATE}SpaceB{@value AddressParser#CHAR_LOCATE}SpaceC{@value AddressParser#CHAR_DATA}ObjectD表示查询根空间下的SpaceA子空间的SpaceB的子空间SpaceC中的数据对象D。
     *
     * @param name 对象名称
     * @return 有空间坐标的以根空间内的子空间坐标开始，最后为对象名称的列表。否则是当前空间对象，只返回对象名称。
     */
    protected Address parseObjectName(String name) throws NameParseException {
        List<Address> addresses = AddressParser.parseAddress(name);
        if (addresses.size() > 1) {
            throw new NameParseException(name + "解析到多个地址！");
        } else if (addresses.size() < 1) {
            throw new NameParseException(name + "未解析到地址！");
        } else {
            return addresses.get(0);
        }
    }

    /**
     * 数据解析，将输入数据源解析成输出数据列表
     *
     * @param dataIn
     * @return
     */
    protected abstract Collection<DOUT> parse(DIN dataIn);

    /**
     * 处理解析后的数据，用这些数据填充空间
     *
     * @param douts
     * @return
     */
    protected abstract void fill(Collection<DOUT> douts);

}
