package com.smartjobandina.user.domain.dto;

import com.smartjobandina.user.domain.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @NotBlank(message = "The name value is empty")
    private String name;

    @NotBlank(message = "The email value is empty")
    private String email;

    @NotBlank(message = "The password value is empty")
    private String password;

    @NotEmpty(message = "The phone list is empty")
    private Set<PhoneDto> phones;

    private Role role;
}
