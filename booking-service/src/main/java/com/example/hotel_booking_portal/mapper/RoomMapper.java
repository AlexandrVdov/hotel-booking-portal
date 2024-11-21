package com.example.hotel_booking_portal.mapper;

import com.example.domain.Room;
import com.example.hotel_booking_portal.web.model.request.UpsertRoomRequest;
import com.example.hotel_booking_portal.web.model.response.RoomFilterListResponse;
import com.example.hotel_booking_portal.web.model.response.RoomListResponse;
import com.example.hotel_booking_portal.web.model.response.RoomResponse;
import com.example.hotel_booking_portal.web.model.response.UpdateRoomResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoomMapper {

    Room requestToRoom(UpsertRoomRequest request);

    @Mapping(source = "roomId", target = "id")
    Room requestToRoom(Long roomId, UpsertRoomRequest request);

    RoomResponse roomToResponse(Room room);

    UpdateRoomResponse roomToUpdateResponse(Room room);

    default RoomListResponse roomListToRoomListResponse(List<Room> rooms) {
        RoomListResponse response = new RoomListResponse();

        response.setRooms(rooms.stream()
                .map(this::roomToResponse)
                .collect(Collectors.toList()));

        return response;
    }

    default RoomFilterListResponse roomListToRoomFilterListResponse(List<Room> rooms) {
        RoomFilterListResponse response = new RoomFilterListResponse();

        response.setRooms(rooms.stream()
                .map(this::roomToResponse)
                .collect(Collectors.toList()));
        response.setTotalRecords(rooms.size());

        return response;
    }
}
