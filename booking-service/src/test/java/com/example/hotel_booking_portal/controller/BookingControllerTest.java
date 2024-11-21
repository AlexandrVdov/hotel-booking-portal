package com.example.hotel_booking_portal.controller;

import com.example.domain.RoleType;
import com.example.domain.UserRole;
import com.example.hotel_booking_portal.AbstractTestController;
import com.example.hotel_booking_portal.StringTestUtils;
import com.example.exception.EntityNotAvailableException;
import com.example.exception.EntityNotFoundException;
import com.example.hotel_booking_portal.service.BookingService;
import com.example.service.UserService;
import com.example.hotel_booking_portal.web.model.request.BookingFilter;
import com.example.hotel_booking_portal.web.model.request.UpsertBookingRequest;
import com.example.hotel_booking_portal.web.model.response.BookingListResponse;
import com.example.hotel_booking_portal.web.model.response.BookingResponse;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BookingControllerTest extends AbstractTestController {

    @MockBean
    private BookingService bookingService;

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
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    public void whenFindAll_thenReturnAllBookings() throws Exception {

        List<BookingResponse> bookingResponses = new ArrayList<>();
        bookingResponses.add(createBookingResponse(1L));
        bookingResponses.add(createBookingResponse(2L));

        BookingListResponse bookingListResponse = new BookingListResponse();
        bookingListResponse.setBookings(bookingResponses);

        BookingFilter filter = new BookingFilter(5, 1);

        Mockito.when(bookingService.findAll(filter))
                .thenReturn(bookingListResponse);

        String actualResponse = mockMvc.perform(get("/api/v1/booking")
                        .param("pageSize", "5")
                        .param("pageNumber", "1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtils
                .readStringFromResource("response/find_all_bookings_response.json");

        Mockito.verify(bookingService, Mockito.times(1)).findAll(filter);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    @WithMockUser(username = "User", roles = {"USER"})
    public void whenCreateBooking_thenReturnNewBooking() throws Exception {
        UpsertBookingRequest request = createUpsertBookingRequest(1L);
        BookingResponse response = createBookingResponse(1L);

        Mockito.when(bookingService.save(request)).thenReturn(response);

        String actualResponse = mockMvc.perform(post("/api/v1/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtils
                .readStringFromResource("response/create_booking_response.json");

        Mockito.verify(bookingService, Mockito.times(1)).save(request);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    @WithMockUser(username = "User", roles = {"USER"})
    public void whenCreateBookingWithWrongHotelId_thenReturnError() throws Exception {
        UpsertBookingRequest request = createUpsertBookingRequest(500L);

        Mockito.when(bookingService.save(request))
                .thenThrow(new EntityNotFoundException("Пользователь не найден с id 500"));

        var response = mockMvc.perform(post("/api/v1/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectedResponse = StringTestUtils.readStringFromResource(
                "response/create_booking_not_found_response.json");

        Mockito.verify(bookingService, Mockito.times(1)).save(request);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    @WithMockUser(username = "User", roles = {"USER"})
    public void whenCreateBookingWithUnavailableDates_thenReturnError() throws Exception {
        UpsertBookingRequest request = createUpsertBookingRequest(1L);

        Mockito.when(bookingService.save(request))
                .thenThrow(new EntityNotAvailableException("Комната недоступна на указанные даты"));

        var response = mockMvc.perform(post("/api/v1/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectedResponse = StringTestUtils.readStringFromResource(
                "response/create_booking_unavailable_dates_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }
}
