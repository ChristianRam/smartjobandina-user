package com.smartjobandina.user.controller;

import com.smartjobandina.user.constants.ResourceEndpoint;
import com.smartjobandina.user.domain.dto.*;
import com.smartjobandina.user.service.IUserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller to with user operations
 *
 * @author <a href="christianram19@hotmail.com">Christian Ramirez</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping(ResourceEndpoint.USER)
public class UserController {

    public static final String MESSAGE_OPERATION_SUCCESSFUL = "Operation Successful";

    private static final String MESSAGE_INCOMING_REGISTER = "Incoming request register method";

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    /**
     * Register a user
     *
     * @param userDto user data transfers objet
     * @return user created
     */
    @PostMapping(ResourceEndpoint.REGISTER)
    public ResponseEntity<ResponseDto<RegisterResponseDto>> register(@Valid @RequestBody UserDto userDto) {
        log.info(MESSAGE_INCOMING_REGISTER);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto<>(userService.registerUser(userDto),
                MESSAGE_OPERATION_SUCCESSFUL));
    }

    /**
     * Authenticate a user
     * @param request user credentials
     * @return response tokens
     */
    @PostMapping(ResourceEndpoint.AUTHENTICATE)
    public ResponseEntity<AuthenticationResponseDto> authenticate(
            @RequestBody AuthenticationRequestDto request
    ) {
        return ResponseEntity.ok(userService.authenticateUser(request));
    }
}
