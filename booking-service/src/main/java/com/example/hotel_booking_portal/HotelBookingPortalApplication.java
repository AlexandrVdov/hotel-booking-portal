package com.example.hotel_booking_portal;

import com.example.config.SecurityConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(SecurityConfiguration.class)
public class HotelBookingPortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelBookingPortalApplication.class, args);
	}

}
