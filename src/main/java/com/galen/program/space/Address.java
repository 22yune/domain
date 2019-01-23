package com.galen.program.space;

/**
 * Created by baogen.zhang on 2018/12/8
 *
 * @author baogen.zhang
 * @date 2018/12/8
 */
public interface Address {

    Address setName(String name);

    String getName();

    Address setNext(Address next);

    Address getNext();

    Address getObject();

    Address getProperty();

    boolean isSpace();

    boolean isObject();

    boolean isProperty();

    boolean isIndex();

    boolean isInclude(Address sub);

    boolean startWith(Address obj);

    boolean endWith(Address obj);

    Address truncate(Address prefix);
}
