package com.smartjobandina.user.exception;

import java.io.Serial;

/**
 * Exception for email not match
 *
 * @author <a href="christianram19@hotmail.com">Christian Ramirez</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public class EmailNotMatchException extends RuntimeException {


    @Serial
    private static final long serialVersionUID = 3347086298873583822L;

    public EmailNotMatchException(String message) {
        super(message);
    }
}