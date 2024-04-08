package com.smartjobandina.user.service.impl;

import com.smartjobandina.user.config.JwtService;
import com.smartjobandina.user.domain.Token;
import com.smartjobandina.user.domain.TokenType;
import com.smartjobandina.user.domain.User;
import com.smartjobandina.user.domain.dto.AuthenticationRequestDto;
import com.smartjobandina.user.domain.dto.AuthenticationResponseDto;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
    private final AuthenticationManager authenticationManager;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, TokenRepository tokenRepository,
                           PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
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
    public AuthenticationResponseDto authenticateUser(AuthenticationRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        return AuthenticationResponseDto.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
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

    /**
     * Save a user token
     * @param user user data
     * @param jwtToken token
     */
    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    /**
     * Revoke all tokens of user
     * @param user user data
     */
    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
