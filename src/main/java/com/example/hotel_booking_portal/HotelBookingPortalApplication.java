package com.example.hotel_booking_portal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class HotelBookingPortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelBookingPortalApplication.class, args);
		log.info("Приложение Hotel Booking Portal успешно запущено!");
	}

}
