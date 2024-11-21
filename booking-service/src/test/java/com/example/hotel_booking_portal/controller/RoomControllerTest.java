package com.example.hotel_booking_portal.controller;

import com.example.domain.RoleType;
import com.example.domain.UserRole;
import com.example.hotel_booking_portal.AbstractTestController;
import com.example.hotel_booking_portal.StringTestUtils;
import com.example.exception.EntityNotFoundException;
import com.example.hotel_booking_portal.service.RoomService;
import com.example.service.UserService;
import com.example.hotel_booking_portal.web.model.request.RoomFilter;
import com.example.hotel_booking_portal.web.model.request.UpsertRoomRequest;
import com.example.hotel_booking_portal.web.model.response.RoomFilterListResponse;
import com.example.hotel_booking_portal.web.model.response.RoomResponse;
import com.example.hotel_booking_portal.web.model.response.UpdateRoomResponse;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RoomControllerTest extends AbstractTestController {

    @MockBean
    private RoomService roomService;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void setup() {
        userService.save(createUpsertUserRequest(1L, "User"), UserRole.from(RoleType.ROLE_USER));
        userService.save(createUpsertUserRequest(2L, "Admin"), UserRole.from(RoleType.ROLE_ADMIN));
    }

    @AfterEach
    public void afterEach() {
        userService.deleteById(1L);
        userService.deleteById(2L);
    }

    @Test
    @WithMockUser(username = "User", roles = {"USER"})
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
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
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
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
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
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    public void whenDeleteRoomById_thenReturnStatusNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/room/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(roomService, Mockito.times(1)).deleteById(1L);
    }

    @Test
    @WithMockUser(username = "User", roles = {"USER"})
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
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
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

    @Test
    @WithMockUser(username = "User", roles = {"USER"})
    public void whenFilterByValidCriteria_thenReturnsFilteredResults() throws Exception {
        List<RoomResponse> roomResponses = new ArrayList<>();
        roomResponses.add(createRoomResponse(1L, "Room1"));
        roomResponses.add(createRoomResponse(2L, "Room2"));

        RoomFilter filter = new RoomFilter(5, 1);
        filter.setMinPrice(100.);
        filter.setMaxPrice(150.);

        RoomFilterListResponse response = new RoomFilterListResponse();
        response.setRooms(roomResponses);
        response.setTotalRecords(2);

        when(roomService.filterBy(filter)).thenReturn(response);

        mockMvc.perform(get("/api/v1/room/filter")
                        .param("pageSize", "5")
                        .param("pageNumber", "1")
                        .param("minPrice", "100.")
                        .param("maxPrice", "150."))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rooms").isArray())
                .andExpect(jsonPath("$.totalRecords").value(response.getTotalRecords()));
    }
}
