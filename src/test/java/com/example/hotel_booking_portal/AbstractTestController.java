package com.example.hotel_booking_portal;

import com.example.hotel_booking_portal.entity.Hotel;
import com.example.hotel_booking_portal.entity.Room;
import com.example.hotel_booking_portal.web.model.request.UpsertBookingRequest;
import com.example.hotel_booking_portal.web.model.request.UpsertHotelRequest;
import com.example.hotel_booking_portal.web.model.request.UpsertRoomRequest;
import com.example.hotel_booking_portal.web.model.request.UpsertUserRequest;
import com.example.hotel_booking_portal.web.model.response.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@Testcontainers
public abstract class AbstractTestController {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected static PostgreSQLContainer postgreSQLContainer;

    static {
        DockerImageName postgres = DockerImageName.parse("postgres:12.3");

        postgreSQLContainer = (PostgreSQLContainer) new PostgreSQLContainer(postgres)
                .withReuse(true);

        postgreSQLContainer.start();
    }

    @DynamicPropertySource
    public static void registerProperties(DynamicPropertyRegistry registry) {
        String jdbcUrl = postgreSQLContainer.getJdbcUrl();

        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.url", () -> jdbcUrl);
    }

    @Autowired
    protected WebApplicationContext context;

    protected Hotel createHotel(Long id, String name) {
        Hotel hotel = new Hotel();
        hotel.setId(id);
        hotel.setName(name);
        hotel.setAnnouncementTitle("Hotel Add");
        hotel.setCity("City");
        hotel.setAddress("Address");
        hotel.setRating(5.);
        hotel.setNumberOfRating(10);
        hotel.setDistanceFromCityCenter(100.);

        return hotel;
    }

    protected HotelResponse createHotelResponse(Long id, Double mark) {
        HotelResponse hotelResponse = new HotelResponse();
        hotelResponse.setId(id);
        hotelResponse.setName("Hotel");
        hotelResponse.setAnnouncementTitle("Hotel Add");
        hotelResponse.setCity("City");
        hotelResponse.setAddress("Address");
        hotelResponse.setRating(mark);
        hotelResponse.setNumberOfRating(10);
        hotelResponse.setDistanceFromCityCenter(100.);

        return hotelResponse;
    }

    protected UpdateHotelResponse createUpdateHotelResponse(Long id, String name) {
        UpdateHotelResponse updateHotelResponse = new UpdateHotelResponse();
        updateHotelResponse.setId(id);
        updateHotelResponse.setName(name);
        updateHotelResponse.setAnnouncementTitle("Hotel Add");
        updateHotelResponse.setCity("City");
        updateHotelResponse.setAddress("Address");
        updateHotelResponse.setDistanceFromCityCenter(100.);

        return updateHotelResponse;
    }

    protected UpsertHotelRequest createUpsertHotelRequest(Long id, String name) {
        UpsertHotelRequest upsertHotelRequest = new UpsertHotelRequest();
        upsertHotelRequest.setName(name);
        upsertHotelRequest.setAnnouncementTitle("Hotel Add");
        upsertHotelRequest.setCity("City");
        upsertHotelRequest.setAddress("Address");
        upsertHotelRequest.setDistanceFromCityCenter(100.);

        return upsertHotelRequest;
    }

    protected Room createRoom(Long id, String name) {
        Room room = new Room();
        room.setId(id);
        room.setName(name);
        room.setDescription("Room text");
        room.setRoomNumber(12);
        room.setPrice(120.3);
        room.setMaxOccupancy(3);

        return room;
    }

    protected RoomResponse createRoomResponse(Long id, String name) {
        HotelResponse hotel = createHotelResponse(1L, 5.);

        RoomResponse roomResponse = new RoomResponse();
        roomResponse.setId(id);
        roomResponse.setName(name);
        roomResponse.setDescription("Room text");
        roomResponse.setRoomNumber(12);
        roomResponse.setPrice(120.3);
        roomResponse.setMaxOccupancy(3);
        roomResponse.setHotel(hotel);

        return roomResponse;
    }

    protected UpdateRoomResponse createUpdateRoomResponse(Long id, String name) {
        UpdateRoomResponse updateRoomResponse = new UpdateRoomResponse();
        updateRoomResponse.setId(id);
        updateRoomResponse.setName(name);
        updateRoomResponse.setDescription("Room text");
        updateRoomResponse.setRoomNumber(12);
        updateRoomResponse.setPrice(120.3);
        updateRoomResponse.setMaxOccupancy(3);

        return updateRoomResponse;
    }

    protected UpsertRoomRequest createUpsertRoomRequest(Long id, String name) {
        UpsertRoomRequest upsertRoomRequest = new UpsertRoomRequest();
        upsertRoomRequest.setName(name);
        upsertRoomRequest.setDescription("Room text");
        upsertRoomRequest.setRoomNumber(12);
        upsertRoomRequest.setPrice(120.3);
        upsertRoomRequest.setMaxOccupancy(3);
        upsertRoomRequest.setHotelId(1L);

        return upsertRoomRequest;
    }

    protected UserResponse createUserResponse(Long id, String username) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(id);
        userResponse.setUsername(username);
        userResponse.setEmail(username.toLowerCase() + "@mail.ru");
        userResponse.setRole("ROLE_USER");

        return userResponse;
    }

    protected UpdateUserResponse createUpdateUserResponse(Long id, String username) {
        UpdateUserResponse updateUserResponse = new UpdateUserResponse();
        updateUserResponse.setId(id);
        updateUserResponse.setUsername(username);
        updateUserResponse.setEmail(username.toLowerCase() + "@mail.ru");

        return updateUserResponse;
    }

    protected UpsertUserRequest createUpsertUserRequest(Long id, String username) {
        UpsertUserRequest upsertUserRequest = new UpsertUserRequest();
        upsertUserRequest.setUsername(username);
        upsertUserRequest.setPassword("123");
        if (username == null) {
            return upsertUserRequest;
        }
        upsertUserRequest.setEmail(username.toLowerCase() + "@mail.ru");

        return upsertUserRequest;
    }

    protected BookingResponse createBookingResponse(Long id) {
        BookingResponse bookingResponse = new BookingResponse();
        bookingResponse.setId(id);
        bookingResponse.setCheckInDate(LocalDate.parse("2024-10-24"));
        bookingResponse.setCheckOutDate(LocalDate.parse("2024-10-27"));
        bookingResponse.setRoomId(id);
        bookingResponse.setUserId(id);

        return bookingResponse;
    }

    protected UpsertBookingRequest createUpsertBookingRequest(Long id) {
        UpsertBookingRequest upsertBookingRequest = new UpsertBookingRequest();
        upsertBookingRequest.setCheckInDate("2024-10-01");
        upsertBookingRequest.setCheckOutDate("2024-10-08");
        upsertBookingRequest.setRoomId(id);
        upsertBookingRequest.setUserId(id);

        return upsertBookingRequest;
    }
}
