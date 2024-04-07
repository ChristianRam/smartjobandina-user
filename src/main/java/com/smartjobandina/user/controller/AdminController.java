package com.smartjobandina.user.controller;

import com.smartjobandina.user.constants.ResourceEndpoint;
import com.smartjobandina.user.domain.dto.ResponseDto;
import com.smartjobandina.user.domain.dto.UserDto;
import com.smartjobandina.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(ResourceEndpoint.ADMIN)
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    public static final String MESSAGE_OPERATION_SUCCESSFUL = "Operation Successful";

    private final IUserService userService;

    public AdminController(IUserService userService) {
        this.userService = userService;
    }

    /**
     * Return a list of all users registered
     * @return UserDto list
     */
    @PreAuthorize("hasAuthority('admin:read')")
    @GetMapping(ResourceEndpoint.FIND_ALL)
    public ResponseEntity<ResponseDto<List<UserDto>>> findAll() {
        return ResponseEntity.ok(new ResponseDto<>(userService.findAll(), MESSAGE_OPERATION_SUCCESSFUL));
    }
}
