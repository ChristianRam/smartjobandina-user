package com.smartjobandina.user.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * General response object
 *
 * @author <a href="christianram19@hotmail.com">Christian Ramirez</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
@Setter
public class ResponseDto<T> implements Serializable {
    private T data;

    private String message;

    public ResponseDto(String message) {
        Objects.requireNonNull(message, "Message must not be null");

        this.message = message;
    }

    public ResponseDto(T data, String message) {
        Objects.requireNonNull(data, "Data must not be null");
        Objects.requireNonNull(message, "Message must not be null");

        this.data = data;
        this.message = message;
    }

    private static final long serialVersionUID = -7457650946067129740L;
}