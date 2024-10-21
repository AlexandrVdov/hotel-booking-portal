package com.example.hotel_booking_portal.controller;

import com.example.hotel_booking_portal.AbstractTestController;
import com.example.hotel_booking_portal.StringTestUtils;
import com.example.hotel_booking_portal.exception.EntityNotFoundException;
import com.example.hotel_booking_portal.service.RoomService;
import com.example.hotel_booking_portal.web.controller.RoomController;
import com.example.hotel_booking_portal.web.model.request.UpsertRoomRequest;
import com.example.hotel_booking_portal.web.model.response.RoomResponse;
import com.example.hotel_booking_portal.web.model.response.UpdateRoomResponse;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoomController.class)
public class RoomControllerTest extends AbstractTestController {

    @MockBean
    private RoomService roomService;

    @Test
    public void whenGetRoomById_thenReturnRoomById() throws Exception {
        RoomResponse roomResponse = createRoomResponse(1L, "Room");

        Mockito.when(roomService.findById(1L)).thenReturn(roomResponse);

        String actualResponse = mockMvc.perform(get("/api/v1/room/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtils
                .readStringFromResource("response/find_room_by_id_response.json");

        Mockito.verify(roomService, Mockito.times(1)).findById(1L);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenCreateRoom_thenReturnNewRoom() throws Exception {
        UpsertRoomRequest request = createUpsertRoomRequest(1L, "New Room");
        UpdateRoomResponse response = createUpdateRoomResponse(1L, "New Room");

        Mockito.when(roomService.save(request)).thenReturn(response);

        String actualResponse = mockMvc.perform(post("/api/v1/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtils
                .readStringFromResource("response/create_room_response.json");

        Mockito.verify(roomService, Mockito.times(1)).save(request);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenUpdateRoom_thenReturnUpdatedRoom() throws Exception {
        UpsertRoomRequest request = createUpsertRoomRequest(1L, "New Room");
        UpdateRoomResponse response = createUpdateRoomResponse(1L, "New Room");

        Mockito.when(roomService.update(1L, request)).thenReturn(response);

        String actualResponse = mockMvc.perform(put("/api/v1/room/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtils
                .readStringFromResource("response/update_room_response.json");

        Mockito.verify(roomService, Mockito.times(1)).update(1L, request);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenDeleteRoomById_thenReturnStatusNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/room/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(roomService, Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void whenFindByIdNotExistedRoom_thenReturnError() throws Exception {
        Mockito.when(roomService.findById(500L))
                .thenThrow(new EntityNotFoundException("Комната не найдена с id 500"));

        var response = mockMvc.perform(get("/api/v1/room/500"))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectedResponse = StringTestUtils.readStringFromResource(
                "response/find_room_by_id_not_found_response.json");

        Mockito.verify(roomService, Mockito.times(1)).findById(500L);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenCreateRoomWithEmptyName_thenReturnError() throws Exception {

        var response = mockMvc.perform(post("/api/v1/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUpsertRoomRequest(1L, null))))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectedResponse = StringTestUtils.readStringFromResource(
                "response/empty_room_name_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }
}
