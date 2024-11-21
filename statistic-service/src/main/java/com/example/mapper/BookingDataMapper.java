package com.example.mapper;

import com.example.entity.BookingData;
import com.example.event.BookingEvent;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookingDataMapper {

    BookingData eventToBookingData(BookingEvent event);
}
