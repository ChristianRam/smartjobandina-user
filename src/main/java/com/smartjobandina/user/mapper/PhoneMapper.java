package com.smartjobandina.user.mapper;

import com.smartjobandina.user.domain.Phone;
import com.smartjobandina.user.domain.dto.PhoneDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PhoneMapper {

    PhoneDto toDto(Phone phone);
    Phone toEntity(PhoneDto phoneDto);
}
