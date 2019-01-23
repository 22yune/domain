package com.galen.program.core.domain;

/**
 * Created by baogen.zhang on 2018/12/8
 *
 * @author baogen.zhang
 * @date 2018/12/8
 */
public interface Path {

    boolean fromRoot();
    boolean fromCurrent();

    int length();

    Path first();

    Path last();

    Identity now();

    Path next();

    Path next(int i);

    Path pre();

    Path pre(int i);

    Path next(Identity identity);

    Path next(int i, Identity identity);

    Path pre(Identity identity);

    Path pre(int i, Identity identity);
}
