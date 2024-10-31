package com.example.SpringSecurity.service;

import com.example.SpringSecurity.dtos.LoginDto;
import com.example.SpringSecurity.dtos.LoginResponseDto;
import com.example.SpringSecurity.dtos.SignUpDto;
import com.example.SpringSecurity.dtos.UserDto;
import com.example.SpringSecurity.entity.UserEntity;
import com.example.SpringSecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserDto signup(SignUpDto signUpDto) {
      //Check user exists or not by it's email
       Optional<UserEntity>  userEntity = userRepository.findByEmail(signUpDto.getEmail());
       if(userEntity.isPresent()){
           throw new RuntimeException("User Already exists");
       }

//       Follow the same steps used for creation
       UserEntity userEntity1 = modelMapper.map(signUpDto, UserEntity.class);
       userEntity1.setPassword(passwordEncoder.encode(userEntity1.getPassword()));
       UserEntity userEntity2 = userRepository.save(userEntity1);
       return modelMapper.map(userEntity2, UserDto.class);

    }

    public LoginResponseDto login(LoginDto loginDto) {
     Authentication authentication =  authenticationManager.authenticate(
             new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword())
     );
     UserEntity userEntity = (UserEntity) authentication.getPrincipal();

        String accessToken = jwtService.generateToken(userEntity);
        String refreshtoken = jwtService.generateRefreshToken(userEntity);


        return new LoginResponseDto(userEntity.getId(),accessToken,refreshtoken);
    }

    public LoginResponseDto refreshtoken(String refreshtoken) {
        Long userId = jwtService.getUserIdFromToken(refreshtoken);

        UserEntity userEntity = userService.getUserFromId(userId).orElseThrow(() -> new RuntimeException("User not found found"));
        String accessToken = jwtService.generateToken(userEntity);
        return new LoginResponseDto(userEntity.getId(), accessToken, refreshtoken);
    }
}
