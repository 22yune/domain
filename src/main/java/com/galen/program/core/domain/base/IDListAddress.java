package com.galen.program.core.domain.base;

import com.galen.program.core.domain.Identity;
import com.galen.program.core.domain.Path;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baogen.zhang on 2019/1/4
 *
 * @author baogen.zhang
 * @date 2019/1/4
 */
public class IDListAddress implements Path {
    private List<Identity> identityList = new ArrayList<Identity>();
    private int index = 0;

    @Override
    public boolean fromRoot() {
        return false;
    }

    @Override
    public boolean fromCurrent() {
        return false;
    }

    @Override
    public int length() {
        return identityList.size();
    }

    @Override
    public Path first() {
        index = 0;
        return this;
    }

    @Override
    public Path last() {
        index = length() - 1;
        return this;
    }

    @Override
    public Identity now() {
        try{
            return identityList.get(index);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public Path next() {
        index++;
        return this;
    }

    @Override
    public Path next(int i) {
        index += i;
        return this;
    }

    @Override
    public Path pre() {
        index++;
        return this;
    }

    @Override
    public Path pre(int i) {
        index-=i;
        return this;
    }

    @Override
    public Path next(Identity identity) {
        identityList.add(index + 1, identity);
        return this;
    }

    @Override
    public Path next(int i, Identity identity) {
        identityList.add(index + i,identity);
        return this;
    }

    @Override
    public Path pre(Identity identity) {
        identityList.add(index ,identity);
        return this;
    }

    @Override
    public Path pre(int i, Identity identity) {
        identityList.add(index + 1 - i,identity);
        return this;
    }

}
