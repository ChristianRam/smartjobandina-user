package com.smartjobandina.user.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponseDto {
    private String id;
    private LocalDateTime createDate;
    private LocalDateTime lastModified;
    private LocalDateTime lastLogin;
    private String accessToken;
    private String refreshToken;
    private Boolean isActive;
}
