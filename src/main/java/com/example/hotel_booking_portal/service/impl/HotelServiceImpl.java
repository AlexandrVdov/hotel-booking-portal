package com.example.hotel_booking_portal.service.impl;

import com.example.hotel_booking_portal.entity.Hotel;
import com.example.hotel_booking_portal.exception.EntityNotFoundException;
import com.example.hotel_booking_portal.mapper.HotelMapper;
import com.example.hotel_booking_portal.repository.HotelRepository;
import com.example.hotel_booking_portal.service.HotelService;
import com.example.hotel_booking_portal.utils.BeanUtils;
import com.example.hotel_booking_portal.web.model.request.HotelFilter;
import com.example.hotel_booking_portal.web.model.request.UpsertHotelRequest;
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
}