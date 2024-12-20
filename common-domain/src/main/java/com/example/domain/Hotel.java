package com.example.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "hotels")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "announcement_title")
    private String announcementTitle;

    @Column(name = "city")
    private String city;

    @Column(name = "address")
    private String address;

    @Column(name = "distance_from_city_center")
    private Double distanceFromCityCenter;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "number_of_rating")
    private Integer numberOfRating;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Room> rooms = new ArrayList<>();
}
