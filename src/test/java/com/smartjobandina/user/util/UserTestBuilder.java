package com.smartjobandina.user.util;

import com.smartjobandina.user.domain.*;
import com.smartjobandina.user.domain.dto.AuthenticationRequestDto;
import com.smartjobandina.user.domain.dto.PhoneDto;
import com.smartjobandina.user.domain.dto.UserDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class UserTestBuilder {

    public static String ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdWFuaXRvcGVyZUBnbWFpbC5jb20iLCJpYXQiOjE3MTI1" +
            "MjE2ODIsImV4cCI6MTcxMjYwODA4Mn0.3x3knTNmVx8MFtWMgdwg078pCQhD0niT-ldmS25Be3c";

    public static String REFRESH_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjaHJpc3RpYW5yYW0xOTA2QGdtYWlsLmNvbSIsImlhdCI" +
            "6MTcxMjUyMDgyMSwiZXhwIjoxNzEzMTI1NjIxfQ.FqmRnOWsc0UwyR3WKK-8jH6fUmFH_N52bLSPgaO36_I";

    private static final DateTimeFormatter format = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static UserDto buildCorrectUserDto() {
        return UserDto.builder()
                .name("Christian Ramirez")
                .email("christianram1906@gmail.com")
                .password("Steven.8912")
                .phones(Set.of(new PhoneDto("1234567", "1", "57")))
                .role(Role.USER)
                .build();
    }

    public static User buildCorrectUser() {
        return User.builder()
                .id("55ae80f4-2c7b-43d0-9549-86c7bf5e8fa7")
                .name("Christian Ramirez")
                .email("christianram1906@gmail.com")
                .password("Steven.8912")
                .phones(Set.of(new Phone()))
                .role(Role.USER)
                .createDate(LocalDateTime.parse("2024-04-07T15:19:34", format))
                .lastModified(LocalDateTime.parse("2024-04-07T15:19:34", format))
                .lastLogin(null)
                .isActive(true)
                .build();
    }

    public static Token buildCorrectToken() {
        return Token.builder()
                .id(1)
                .token(ACCESS_TOKEN)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .user(buildCorrectUser())
                .build();
    }

    public static AuthenticationRequestDto buildCorrectAuthenticationRequestDto() {
        return AuthenticationRequestDto.builder()
                .email("christianram1906@gmail.com")
                .password("Steven.8912")
                .build();
    }
}
