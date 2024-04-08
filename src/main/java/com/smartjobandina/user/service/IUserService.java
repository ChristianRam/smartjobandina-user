package com.smartjobandina.user.service;

import com.smartjobandina.user.domain.dto.AuthenticationRequestDto;
import com.smartjobandina.user.domain.dto.AuthenticationResponseDto;
import com.smartjobandina.user.domain.dto.RegisterResponseDto;
import com.smartjobandina.user.domain.dto.UserDto;

import java.util.List;

/**
 * @author <a href="christianram19@hotmail.com">Christian Ramirez</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public interface IUserService {

    /**
     * Method to register a user
     * @param userDto user to be registered
     * @return user registered
     */
    RegisterResponseDto registerUser(UserDto userDto);

    /**
     * Authenticate user
     * @param request user credentials
     * @return response tokens
     */
    AuthenticationResponseDto authenticateUser(AuthenticationRequestDto request);


    /**
     * Find all users
     * @return list of users
     */
    List<UserDto> findAll();
}
