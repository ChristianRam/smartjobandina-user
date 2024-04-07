package com.smartjobandina.user.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhoneDto {
    @NotBlank(message = "The number value is empty")
    private String number;
    @NotBlank(message = "The citycode value is empty")
    private String citycode;
    @NotBlank(message = "The countrycode value is empty")
    private String countrycode;
}
