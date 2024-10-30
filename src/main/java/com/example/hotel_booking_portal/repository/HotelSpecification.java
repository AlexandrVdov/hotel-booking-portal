package com.example.hotel_booking_portal.repository;

import com.example.hotel_booking_portal.entity.Hotel;
import com.example.hotel_booking_portal.web.model.request.HotelFilter;
import org.springframework.data.jpa.domain.Specification;

public interface HotelSpecification {

    static Specification<Hotel> withFilter(HotelFilter hotelFilter) {
        return Specification.where(byHotelId(hotelFilter.getId()))
                .and(byName(hotelFilter.getName()))
                .and(byAnnouncementTitle(hotelFilter.getAnnouncementTitle()))
                .and(byCity(hotelFilter.getCity()))
                .and(byAddres(hotelFilter.getAddress()))
                .and(byDistanceFromCityCenter(hotelFilter.getDistanceFromCityCenter()))
                .and(byRating(hotelFilter.getRating()))
                .and(byNumberOfRating(hotelFilter.getNumberOfRating()));
    }

    static Specification<Hotel> byHotelId(Long id) {
        return (root, query, cb) -> {
            if (id == null) {
                return null;
            }

            return cb.equal(root.get("id"), id);
        };
    }

    static Specification<Hotel> byName(String name) {
        return (root, query, cb) -> {
            if (name == null) {
                return null;
            }

            return cb.equal(root.get("name"), name);
        };
    }

    static Specification<Hotel> byAnnouncementTitle(String announcementTitle) {
        return (root, query, cb) -> {
            if (announcementTitle == null) {
                return null;
            }

            return cb.equal(root.get("announcementTitle"), announcementTitle);
        };
    }

    static Specification<Hotel> byCity(String city) {
        return (root, query, cb) -> {
            if (city == null) {
                return null;
            }

            return cb.equal(root.get("city"), city);
        };
    }

    static Specification<Hotel> byAddres(String address) {
        return (root, query, cb) -> {
            if (address == null) {
                return null;
            }

            return cb.equal(root.get("address"), address);
        };
    }

    static Specification<Hotel> byDistanceFromCityCenter(Double distanceFromCityCenter) {
        return (root, query, cb) -> {
            if (distanceFromCityCenter == null) {
                return null;
            }

            return cb.equal(root.get("distanceFromCityCenter"), distanceFromCityCenter);
        };
    }

    static Specification<Hotel> byRating(Double rating) {
        return (root, query, cb) -> {
            if (rating == null) {
                return null;
            }

            return cb.equal(root.get("rating"), rating);
        };
    }

    static Specification<Hotel> byNumberOfRating(Integer numberOfRating) {
        return (root, query, cb) -> {
            if (numberOfRating == null) {
                return null;
            }

            return cb.equal(root.get("numberOfRating"), numberOfRating);
        };
    }
}
