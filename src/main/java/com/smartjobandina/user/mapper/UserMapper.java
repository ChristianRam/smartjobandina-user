package com.smartjobandina.user.mapper;

import com.smartjobandina.user.domain.User;
import com.smartjobandina.user.domain.dto.RegisterResponseDto;
import com.smartjobandina.user.domain.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);
    User toEntity(UserDto userDto);

    @Mapping(target = "accessToken", source = "accessToken")
    @Mapping(target = "refreshToken", source = "refreshToken")
    RegisterResponseDto userToRegisterResponseDto(User user, String accessToken, String refreshToken);
}
