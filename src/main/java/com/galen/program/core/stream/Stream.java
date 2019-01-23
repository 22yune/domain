package com.galen.program.core.stream;

import com.galen.program.core.domain.Entity;

/**
 * Created by baogen.zhang on 2019/1/14
 *
 * @author baogen.zhang
 * @date 2019/1/14
 */
public interface Stream {

    Entity current();

    Stream next();
}
