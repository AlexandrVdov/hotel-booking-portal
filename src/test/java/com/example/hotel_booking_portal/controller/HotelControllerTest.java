package com.example.hotel_booking_portal.controller;

import com.example.hotel_booking_portal.AbstractTestController;
import com.example.hotel_booking_portal.StringTestUtils;
import com.example.hotel_booking_portal.entity.RoleType;
import com.example.hotel_booking_portal.entity.UserRole;
import com.example.hotel_booking_portal.exception.EntityNotFoundException;
import com.example.hotel_booking_portal.service.HotelService;
import com.example.hotel_booking_portal.service.UserService;
import com.example.hotel_booking_portal.web.model.request.HotelFilter;
import com.example.hotel_booking_portal.web.model.request.UpsertHotelRequest;
import com.example.hotel_booking_portal.web.model.response.HotelFilterListResponse;
import com.example.hotel_booking_portal.web.model.response.HotelListResponse;
import com.example.hotel_booking_portal.web.model.response.HotelResponse;
import com.example.hotel_booking_portal.web.model.response.UpdateHotelResponse;
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

public class HotelControllerTest extends AbstractTestController {

    @MockBean
    private HotelService hotelService;

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
    public void whenFindAll_thenReturnAllHotels() throws Exception {

        List<HotelResponse> hotelResponses = new ArrayList<>();
        hotelResponses.add(createHotelResponse(1L, 5.));
        hotelResponses.add(createHotelResponse(2L, 5.));

        HotelListResponse hotelListResponse = new HotelListResponse();
        hotelListResponse.setHotels(hotelResponses);

        HotelFilter filter = new HotelFilter(5, 1);

        when(hotelService.findAll(filter))
                .thenReturn(hotelListResponse);

        String actualResponse = mockMvc.perform(get("/api/v1/hotel")
                .param("pageSize", "5")
                .param("pageNumber", "1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtils
                .readStringFromResource("response/find_all_hotels_response.json");

        Mockito.verify(hotelService, Mockito.times(1)).findAll(filter);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    @WithMockUser(username = "User", roles = {"USER"})
    public void whenGetHotelById_thenReturnHotelById() throws Exception {
        HotelResponse hotelResponse = createHotelResponse(1L, 5.);

        when(hotelService.findById(1L)).thenReturn(hotelResponse);

        String actualResponse = mockMvc.perform(get("/api/v1/hotel/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtils
                .readStringFromResource("response/find_hotel_by_id_response.json");

        Mockito.verify(hotelService, Mockito.times(1)).findById(1L);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    public void whenCreateHotel_thenReturnNewHotel() throws Exception {
        UpsertHotelRequest request = createUpsertHotelRequest(1L, "New Hotel");
        UpdateHotelResponse response = createUpdateHotelResponse(1L, "New Hotel");

        when(hotelService.save(request)).thenReturn(response);

        String actualResponse = mockMvc.perform(post("/api/v1/hotel")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtils
                .readStringFromResource("response/create_hotel_response.json");

        Mockito.verify(hotelService, Mockito.times(1)).save(request);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    public void whenUpdateHotel_thenReturnUpdatedHotel() throws Exception {
        UpsertHotelRequest request = createUpsertHotelRequest(1L, "New Hotel");
        UpdateHotelResponse response = createUpdateHotelResponse(1L, "New Hotel");

        when(hotelService.update(1L, request)).thenReturn(response);

        String actualResponse = mockMvc.perform(put("/api/v1/hotel/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtils
                .readStringFromResource("response/update_hotel_response.json");

        Mockito.verify(hotelService, Mockito.times(1)).update(1L, request);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    public void whenDeleteHotelById_thenReturnStatusNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/hotel/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(hotelService, Mockito.times(1)).deleteById(1L);
    }

    @Test
    @WithMockUser(username = "User", roles = {"USER"})
    public void whenFindByIdNotExistedHotel_thenReturnError() throws Exception {
        when(hotelService.findById(500L))
                .thenThrow(new EntityNotFoundException("Отель не найден с id 500"));

        var response = mockMvc.perform(get("/api/v1/hotel/500"))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectedResponse = StringTestUtils.readStringFromResource(
                "response/find_hotel_by_id_not_found_response.json");

        Mockito.verify(hotelService, Mockito.times(1)).findById(500L);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    public void whenCreateHotelWithEmptyName_thenReturnError() throws Exception {

        var response = mockMvc.perform(post("/api/v1/hotel")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUpsertHotelRequest(1L, null))))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectedResponse = StringTestUtils.readStringFromResource(
                "response/empty_hotel_name_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    public void whenUpdateRating_thenReturnUpdatedHotel() throws Exception {
        Long hotelId = 1L;
        int newMark = 4;

        HotelResponse hotelResponse = createHotelResponse(hotelId, (double) newMark);

        when(hotelService.updateRating(hotelId, newMark)).thenReturn(hotelResponse);

        mockMvc.perform(put("/api/v1/hotel/{id}/rating", hotelId)
                        .param("newMark", String.valueOf(newMark))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(newMark));
    }

    @Test
    @WithMockUser(username = "User", roles = {"USER"})
    public void whenUpdateRatingWithInvalidValue_thenReturnBadRequest() throws Exception {
        Long hotelId = 1L;
        int invalidMark = 6;

        mockMvc.perform(put("/api/v1/hotel/{id}/rating", hotelId)
                        .param("newMark", String.valueOf(invalidMark))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "User", roles = {"USER"})
    public void whenFilterByValidCriteria_thenReturnsFilteredResults() throws Exception {
        List<HotelResponse> hotelResponses = new ArrayList<>();
        hotelResponses.add(createHotelResponse(1L, 5.));
        hotelResponses.add(createHotelResponse(2L, 5.));

        HotelFilter filter = new HotelFilter(5, 1);
        filter.setCity("City");

        HotelFilterListResponse response = new HotelFilterListResponse();
        response.setHotels(hotelResponses);
        response.setTotalRecords(2);

        when(hotelService.filterBy(filter)).thenReturn(response);

        mockMvc.perform(get("/api/v1/hotel/filter")
                        .param("pageSize", "5")
                        .param("pageNumber", "1")
                        .param("city", "City"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hotels").isArray())
                .andExpect(jsonPath("$.totalRecords").value(response.getTotalRecords()));
    }
}
