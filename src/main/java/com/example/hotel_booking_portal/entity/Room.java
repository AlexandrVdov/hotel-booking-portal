package com.example.hotel_booking_portal.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@Entity(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "room_number")
    private Integer roomNumber;

    @Column(name = "price")
    private Double price;

    @Column(name = "max_occupancy")
    private Integer maxOccupancy;

    @ElementCollection
    @CollectionTable(name = "room_unavailability", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "unavailable_dates")
    private List<Date> unavailableDates;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    @ToString.Exclude
    private Hotel hotel;

}
