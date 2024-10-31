package com.example.SpringSecurity.controllers;

import com.example.SpringSecurity.dtos.LoginDto;
import com.example.SpringSecurity.dtos.LoginResponseDto;
import com.example.SpringSecurity.dtos.SignUpDto;
import com.example.SpringSecurity.dtos.UserDto;
import com.example.SpringSecurity.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.util.Arrays;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignUpDto signUpDto){
        UserDto userDto = authService.signup(signUpDto);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto, HttpServletResponse response){
        LoginResponseDto  loginResponseDto = authService.login(loginDto);
        Cookie cookie = new Cookie("refreshToken", loginResponseDto.getRefreshToken());
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return ResponseEntity.ok(loginResponseDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> generateRefreshToken(HttpServletRequest request){
      String refreshtoken =  Arrays.stream(request.getCookies())
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
              .map(Cookie::getValue)
                .orElseThrow(()-> new RuntimeException("Refresh Token Not Found inside the cookies"));

      LoginResponseDto loginResponseDto = authService.refreshtoken(refreshtoken);
        return ResponseEntity.ok(loginResponseDto);
    }
}
