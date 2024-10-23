package com.example.hotel_booking_portal.service.impl;

import com.example.hotel_booking_portal.entity.Hotel;
import com.example.hotel_booking_portal.entity.Room;
import com.example.hotel_booking_portal.exception.EntityNotFoundException;
import com.example.hotel_booking_portal.mapper.RoomMapper;
import com.example.hotel_booking_portal.repository.HotelRepository;
import com.example.hotel_booking_portal.repository.RoomRepository;
import com.example.hotel_booking_portal.service.RoomService;
import com.example.hotel_booking_portal.utils.BeanUtils;
import com.example.hotel_booking_portal.web.model.request.UpsertRoomRequest;
import com.example.hotel_booking_portal.web.model.response.RoomListResponse;
import com.example.hotel_booking_portal.web.model.response.RoomResponse;
import com.example.hotel_booking_portal.web.model.response.UpdateRoomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    private final RoomMapper roomMapper;

    private final HotelRepository hotelRepository;

    @Override
    public RoomResponse findById(Long roomId) {
        return roomMapper.roomToResponse(
                roomRepository.findById(roomId)
                        .orElseThrow(() -> new EntityNotFoundException(
                                MessageFormat.format("Комната не найдена с id {0}", roomId)
                        ))
        );
    }

    @Override
    public UpdateRoomResponse save(UpsertRoomRequest request) {
        Room newRoom = roomMapper.requestToRoom(request);

        Hotel existedHotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageFormat.format("Комната не найдена с id {0}", request.getHotelId())
                ));
        newRoom.setHotel(existedHotel);

        return roomMapper.roomToUpdateResponse(roomRepository.save(newRoom));
    }

    @Override
    public UpdateRoomResponse update(Long roomId, UpsertRoomRequest request) {
        Room updatedRoom = roomMapper.requestToRoom(roomId, request);

        Hotel existedHotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageFormat.format("Отель не найден с id {0}", request.getHotelId())
                ));
        Room existedRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageFormat.format("Комната не найдена с id {0}", roomId)
                ));
        BeanUtils.copyNonNullProperties(updatedRoom, existedRoom);
        existedRoom.setHotel(existedHotel);

        return roomMapper.roomToUpdateResponse(roomRepository.save(existedRoom));
    }

    @Override
    public void deleteById(Long roomId) {
        roomRepository.deleteById(roomId);
    }
}
