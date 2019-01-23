package com.galen.program.space;

/**
 * Created by baogen.zhang on 2018/11/23
 *
 * @author baogen.zhang
 * @date 2018/11/23
 */
public class UnableToRegisterException extends RuntimeException {

    public UnableToRegisterException() {
        super();
    }

    public UnableToRegisterException(String message) {
        super(message);
    }
}
