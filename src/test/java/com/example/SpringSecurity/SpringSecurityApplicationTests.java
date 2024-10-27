package com.example.SpringSecurity;

import com.example.SpringSecurity.entity.UserEntity;
import com.example.SpringSecurity.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringSecurityApplicationTests {

	@Autowired
	private JwtService jwtService;

	@Test
	void contextLoads() {
		UserEntity userEntity = new UserEntity(4l,"jay@gmail.com","1234","JAy");

		String token = jwtService.generateToken(userEntity);

		System.out.println(token);

		Long userId = jwtService.getUserIdFromToken(token);

		System.out.println("UserId"+userId);
	}

}
