package com.galen.program.space;

/**
 * Created by baogen.zhang on 2018/12/11
 *
 * @author baogen.zhang
 * @date 2018/12/11
 */
public class DefineFormatException extends RuntimeException {
    public DefineFormatException(String message) {
        super(message);
    }

    public DefineFormatException(String message, Exception e) {
        super(message, e);
    }
}
