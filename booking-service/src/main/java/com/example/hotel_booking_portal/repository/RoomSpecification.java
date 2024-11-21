package com.example.hotel_booking_portal.repository;

import com.example.domain.Room;
import com.example.hotel_booking_portal.web.model.request.RoomFilter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

public interface RoomSpecification {

    static Specification<Room> withFilter(RoomFilter roomFilter) {
        return Specification.where(byRoomId(roomFilter.getId()))
                .and(byTitle(roomFilter.getTitle()))
                .and(byCostRange(roomFilter.getMinPrice(), roomFilter.getMaxPrice()))
                .and(byGuestsNumber(roomFilter.getGuestsNumber()))
                .and(byCheckDateRange(roomFilter.getCheckInDate(), roomFilter.getCheckOutDate()))
                .and(byHotelId(roomFilter.getHotelId()));
    }

    static Specification<Room> byRoomId(Long id) {
        return (root, query, cb) -> {
            if (id == null) {
                return null;
            }

            return cb.equal(root.get("id"), id);
        };
    }

    static Specification<Room> byTitle(String title) {
        return (root, query, cb) -> {
            if (title == null) {
                return null;
            }

            return cb.equal(root.get("name"), title);
        };
    }

    static Specification<Room> byCostRange(Double minPrice, Double maxPrice) {
        return (root, query, cb) -> {
            if (minPrice == null && maxPrice == null) {
                return null;
            }

            if (minPrice == null) {
                return cb.lessThanOrEqualTo(root.get("price"), maxPrice);
            }

            if (maxPrice == null) {
                return cb.greaterThanOrEqualTo(root.get("price"), minPrice);
            }

            return cb.between(root.get("price"), minPrice, maxPrice);
        };
    }

    static Specification<Room> byGuestsNumber(Integer guestsNumber) {
        return (root, query, cb) -> {
            if (guestsNumber == null) {
                return null;
            }

            return cb.greaterThan(root.get("maxOccupancy"), guestsNumber);
        };
    }

    static Specification<Room> byCheckDateRange(LocalDate checkInDate, LocalDate checkOutDate) {
        return (root, query, cb) -> {
            if (checkInDate == null || checkOutDate == null) {
                return null;
            }
            List<LocalDate> checkDates = checkInDate.datesUntil(checkOutDate).toList();

            Predicate[] predicates = new Predicate[checkDates.size()];
            for (int i = 0; i < checkDates.size(); i++) {
                predicates[i] = cb.isNotMember(checkDates.get(i), root.get("unavailableDates"));
            }

            return cb.and(predicates);
        };
    }

    static Specification<Room> byHotelId(Long hotelId) {
        return (root, query, cb) -> {
            if (hotelId == null) {
                return null;
            }

            return cb.equal(root.get("hotelId"), hotelId);
        };
    }
}
