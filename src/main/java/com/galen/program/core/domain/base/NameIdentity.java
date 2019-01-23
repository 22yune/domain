package com.galen.program.core.domain.base;

import com.galen.program.core.domain.Identity;


/**
 * Created by baogen.zhang on 2019/1/4
 *
 * @author baogen.zhang
 * @date 2019/1/4
 */
public class NameIdentity implements Identity {
    private String name;

    public NameIdentity(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object){
            return true;
        }
        if (object == null || getClass() != object.getClass()){
            return false;
        }
        NameIdentity that = (NameIdentity) object;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "NameIdentity{" +
                "name='" + name + '\'' +
                '}';
    }
}
