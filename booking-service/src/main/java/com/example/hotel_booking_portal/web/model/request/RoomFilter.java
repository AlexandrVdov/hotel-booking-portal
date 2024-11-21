package com.example.hotel_booking_portal.web.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomFilter {

    @NotNull(message = "Не указан параметр кол-во на страницу")
    private Integer pageSize;

    @NotNull(message = "Не указан параметр номер страницы")
    private Integer pageNumber;

    private Long id;

    private String title;

    private Double minPrice;

    private Double maxPrice;

    private Integer guestsNumber;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private Long hotelId;

    public RoomFilter(Integer pageSize, Integer pageNumber) {
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
    }
}
