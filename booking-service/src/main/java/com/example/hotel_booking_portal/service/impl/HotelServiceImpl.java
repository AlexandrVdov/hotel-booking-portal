package com.example.hotel_booking_portal.service.impl;

import com.example.domain.Hotel;
import com.example.exception.EntityNotFoundException;
import com.example.hotel_booking_portal.mapper.HotelMapper;
import com.example.hotel_booking_portal.repository.HotelRepository;
import com.example.hotel_booking_portal.repository.HotelSpecification;
import com.example.hotel_booking_portal.service.HotelService;
import com.example.utils.BeanUtils;
import com.example.hotel_booking_portal.web.model.request.HotelFilter;
import com.example.hotel_booking_portal.web.model.request.UpsertHotelRequest;
import com.example.hotel_booking_portal.web.model.response.HotelFilterListResponse;
import com.example.hotel_booking_portal.web.model.response.HotelListResponse;
import com.example.hotel_booking_portal.web.model.response.HotelResponse;
import com.example.hotel_booking_portal.web.model.response.UpdateHotelResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;

    private final HotelMapper hotelMapper;

    @Override
    public HotelListResponse findAll(HotelFilter filter) {
        return hotelMapper.hotelListToHotelListResponse(
                hotelRepository.findAll(
                        PageRequest.of(filter.getPageNumber(), filter.getPageSize())
                ).getContent()
        );
    }

    @Override
    public HotelFilterListResponse filterBy(HotelFilter filter) {
        return hotelMapper.hotelListToHotelFilterListResponse(hotelRepository.findAll(
                        HotelSpecification.withFilter(filter),
                        PageRequest.of(filter.getPageNumber(), filter.getPageSize())
                ).getContent()
        );
    }

    @Override
    public HotelResponse findById(Long hotelId) {
        return hotelMapper.hotelToResponse(
                hotelRepository.findById(hotelId)
                        .orElseThrow(() -> new EntityNotFoundException(
                                MessageFormat.format("Отель не найден с id {0}", hotelId)
                        ))
        );
    }

    @Override
    public UpdateHotelResponse save(UpsertHotelRequest request) {
        Hotel newHotel = hotelMapper.requestToHotel(request);

        return hotelMapper.hotelToUpdateResponse(hotelRepository.save(newHotel));
    }

    @Override
    public UpdateHotelResponse update(Long hotelId, UpsertHotelRequest request) {
        Hotel updatedHotel = hotelMapper.requestToHotel(hotelId, request);

        Hotel existedHotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageFormat.format("Отель не найден с id {0}", hotelId)
                ));
        BeanUtils.copyNonNullProperties(updatedHotel, existedHotel);

        return hotelMapper.hotelToUpdateResponse(hotelRepository.save(existedHotel));
    }

    @Override
    public void deleteById(Long hotelId) {
        hotelRepository.deleteById(hotelId);
    }

    @Override
    public HotelResponse updateRating(Long hotelId, Integer newMark) {
        Hotel existedHotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageFormat.format("Отель не найден с id {0}", hotelId)
                ));

        setNewRating(existedHotel, newMark);

        return hotelMapper.hotelToResponse(hotelRepository.save(existedHotel));
    }

    private void setNewRating(Hotel hotel, Integer newMark) {
        Double rating = hotel.getRating();
        Integer numberOfRating = hotel.getNumberOfRating();

        if (rating == null) {
            rating = Double.valueOf(newMark);
            numberOfRating = Integer.valueOf("0");

        } else {
            double totalRating = rating * numberOfRating;
            totalRating = totalRating - rating + newMark;
            rating = totalRating / numberOfRating;
        }

        rating = Math.round(rating * 10.0) / 10.0;
        numberOfRating = numberOfRating + 1;

        hotel.setRating(rating);
        hotel.setNumberOfRating(numberOfRating);
    }
}
