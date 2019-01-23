package com.galen.program.plugin;

/**
 * Created by baogen.zhang on 2018/11/26
 *
 * @author baogen.zhang
 * @date 2018/11/26
 */
public class PluginException extends RuntimeException {
    public PluginException() {
        super();
    }

    public PluginException(String message) {
        super(message);
    }

    public PluginException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginException(Throwable cause) {
        super(cause);
    }
}
