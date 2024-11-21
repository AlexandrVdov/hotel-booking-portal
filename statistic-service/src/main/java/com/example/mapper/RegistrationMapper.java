package com.example.mapper;

import com.example.entity.Registration;
import com.example.event.RegistrationEvent;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RegistrationMapper {

    Registration eventToRegistrationData(RegistrationEvent event);
}
