package com.smartjobandina.user.exception;

import java.io.Serial;

/**
 * Exception for password not match
 *
 * @author <a href="christianram19@hotmail.com">Christian Ramirez</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public class PasswordNotMatchException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 9134211280932300122L;

    public PasswordNotMatchException(String message) {
        super(message);
    }
}

