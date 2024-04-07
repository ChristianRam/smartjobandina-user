package com.smartjobandina.user.service.impl;

import com.smartjobandina.user.config.JwtService;
import com.smartjobandina.user.domain.Token;
import com.smartjobandina.user.domain.TokenType;
import com.smartjobandina.user.domain.User;
import com.smartjobandina.user.domain.dto.RegisterResponseDto;
import com.smartjobandina.user.domain.dto.UserDto;
import com.smartjobandina.user.exception.EmailAlrealdyRegisteredException;
import com.smartjobandina.user.exception.EmailNotMatchException;
import com.smartjobandina.user.exception.PasswordNotMatchException;
import com.smartjobandina.user.mapper.UserMapper;
import com.smartjobandina.user.repository.TokenRepository;
import com.smartjobandina.user.repository.UserRepository;
import com.smartjobandina.user.service.IUserService;
import com.smartjobandina.user.util.RegexValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * User service
 *
 * @author <a href="christianram19@hotmail.com">Christian Ramirez</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    private static final String MESSAGE_PASSWORD_NOT_MATCH_ERROR = "Invalid password format";
    private static final String MESSAGE_EMAIL_NOT_MATCH_ERROR = "Invalid email format";
    private static final String MESSAGE_EMAIL_ALREADY_REGISTERED_ERROR = "Email already registered";

    @Value("${user.password.regex}")
    private String passwordRegex;

    @Value("${user.email.regex}")
    private String emailRegex;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, TokenRepository tokenRepository,
                           PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RegisterResponseDto registerUser(UserDto userDto) {
        validateEmail(userDto.getEmail());
        validatePassword(userDto.getPassword());

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        User user = userRepository.save(userMapper.toEntity(userDto));
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(user, jwtToken);

        return userMapper.userToRegisterResponseDto(user, jwtToken, refreshToken);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Validates if email format is correct and if email doesn't exist
     *
     * @param email email to validate
     */
    private void validateEmail(String email) {
        if (!RegexValidator.patternMatches(email, emailRegex)) {
            throw new EmailNotMatchException(MESSAGE_EMAIL_NOT_MATCH_ERROR);
        }

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlrealdyRegisteredException(MESSAGE_EMAIL_ALREADY_REGISTERED_ERROR);
        }
    }

    /**
     * Validates if password format is correct
     *
     * @param password password to validate
     */
    private void validatePassword(String password) {
        if (!RegexValidator.patternMatches(password, passwordRegex)) {
            throw new PasswordNotMatchException(MESSAGE_PASSWORD_NOT_MATCH_ERROR);
        }
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }
}
