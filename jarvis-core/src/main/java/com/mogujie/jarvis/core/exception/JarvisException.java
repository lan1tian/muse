package com.mogujie.jarvis.core.exception;

/**
 * @author muming
 *
 */
public class JarvisException extends Exception{

    private static final long serialVersionUID = 1L;

    private  String message;

    public JarvisException() {
        super();
    }

    public JarvisException(String message) {
        super(message);
    }

    public JarvisException(String message, Throwable cause) {
        super(message, cause);
    }

    public JarvisException(Throwable cause) {
        super(cause);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message == null ? super.getMessage() : this.message;
    }

    @Override
    public String toString() {
        return this.message;
    }


}
