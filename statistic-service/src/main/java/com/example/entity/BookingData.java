package com.example.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Data
@Document(collection = "bookings")
public class BookingData {

    @Id
    private String id;

    @Field("user_id")
    private Long userId;

    @Field("check_in_date")
    private LocalDate checkInDate;

    @Field("check_out_date")
    private LocalDate checkOutDate;
}
