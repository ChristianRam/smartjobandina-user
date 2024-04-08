package com.smartjobandina.user.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ResourceEndpoint {

    /**
     * Endpoint to operator
     */
    public static final String USER = "/users/auth";

    /**
     * Endpoint to maximum
     */
    public static final String REGISTER = "/register";

    public static final String AUTHENTICATE = "/authenticate";
    public static final String LOGOUT = "/logout";


    public static final String ADMIN = "/admin";
    public static final String FIND_ALL = "/find-all";
}