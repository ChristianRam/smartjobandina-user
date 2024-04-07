package com.smartjobandina.user.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static com.smartjobandina.user.util.UserTestBuilder.*;

import com.smartjobandina.user.config.JwtService;
import com.smartjobandina.user.domain.Token;
import com.smartjobandina.user.domain.User;
import com.smartjobandina.user.domain.dto.RegisterResponseDto;
import com.smartjobandina.user.domain.dto.UserDto;
import com.smartjobandina.user.exception.EmailAlrealdyRegisteredException;
import com.smartjobandina.user.exception.EmailNotMatchException;
import com.smartjobandina.user.exception.PasswordNotMatchException;
import com.smartjobandina.user.mapper.UserMapper;
import com.smartjobandina.user.repository.TokenRepository;
import com.smartjobandina.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(SpringExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Mock
    private TokenRepository tokenRepository;

    @Spy
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(userService, "passwordRegex",
                "^(?=.*[0-9])(?=.*[a-zA-Z]).{7,}$");
        ReflectionTestUtils.setField(userService, "emailRegex",
                "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
    }

    @Test
    void givenCorrectUserDto_WhenCallRegisterUser_ThenReturnObjectSuccessfully() {
        // Given
        UserDto userDto = buildCorrectUserDto();
        User user = buildCorrectUser();
        Token token = buildCorrectToken();
        //When
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);
        when(jwtService.generateToken(any())).thenReturn(ACCESS_TOKEN);
        when(jwtService.generateRefreshToken(any())).thenReturn(REFRESH_TOKEN);
        when(tokenRepository.save(any())).thenReturn(token);

        RegisterResponseDto responseDto = userService.registerUser(userDto);

        //Then
        assertEquals(user.getId(), responseDto.getId());
        assertEquals(user.getCreateDate(), responseDto.getCreateDate());
        assertEquals(user.getLastModified(), responseDto.getLastModified());
        assertNull(responseDto.getLastLogin());
        assertEquals(ACCESS_TOKEN, responseDto.getAccessToken());
        assertEquals(REFRESH_TOKEN, responseDto.getRefreshToken());
        assertEquals(user.getIsActive(), responseDto.getIsActive());
    }

    @Test
    void givenIncorrectEmailFormat_WhenCallRegisterUser_ThenReturnEmailNotMatchException() {
        // Given
        UserDto userDto = buildCorrectUserDto();
        userDto.setEmail("abcsd.com");
        //When
        EmailNotMatchException exception = assertThrows(EmailNotMatchException.class,
                () -> userService.registerUser(userDto));
        //Then
        assertEquals("Invalid email format", exception.getMessage());
    }

    @Test
    void givenEmailAlreadyRegistered_WhenCallRegisterUser_ThenReturnEmailAlrealdyRegisteredException() {
        // Given
        UserDto userDto = buildCorrectUserDto();
        //When
        when(userRepository.existsByEmail(any())).thenReturn(true);
        EmailAlrealdyRegisteredException exception = assertThrows(EmailAlrealdyRegisteredException.class,
                () -> userService.registerUser(userDto));
        //Then
        assertEquals("Email already registered", exception.getMessage());
    }

    @Test
    void givenIncorrectPasswordFormat_WhenCallRegisterUser_ThenReturnPasswordNotMatchException() {
        // Given
        UserDto userDto = buildCorrectUserDto();
        userDto.setPassword("asq1");
        //When
        PasswordNotMatchException exception = assertThrows(PasswordNotMatchException.class,
                () -> userService.registerUser(userDto));
        //Then
        assertEquals("Invalid password format", exception.getMessage());
    }
}
