package ru.gazpromneft.vk_user_membership_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class VkUserMembershipServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(VkUserMembershipServiceApplication.class, args);
	}

}
