package com.galen.program.space;

/**
 * Created by baogen.zhang on 2018/11/25
 *
 * @author baogen.zhang
 * @date 2018/11/25
 */
public class NameParseException extends RuntimeException {
    public NameParseException(String message) {
        super(message);
    }

    public NameParseException(String message, Exception e) {
        super(message, e);
    }
}
