package com.example.hotel_booking_portal.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "hotels")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "title_ad")
    private String titleAd;

    @Column(name = "city")
    private String city;

    @Column(name = "address")
    private String address;

    @Column(name = "distance_from_center")
    private Integer distanceFromCenter;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "number_ratings")
    private Integer numberRatings;
}
