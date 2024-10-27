package com.example.SpringSecurity.filters;

import com.example.SpringSecurity.entity.UserEntity;
import com.example.SpringSecurity.service.JwtService;
import com.example.SpringSecurity.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestHeaderToken = request.getHeader("Authorization");
        if(requestHeaderToken == null || !requestHeaderToken.startsWith("Bearer")){
            filterChain.doFilter(request,response);
            return;
        }

        //Instead of splitting it with "Bearer" do that with " " emptyspace it will work
        String token = requestHeaderToken.split(" ")[1];
        Long userId = jwtService.getUserIdFromToken(token);

        if(userId != null && SecurityContextHolder.getContext().getAuthentication() == null){
            Optional<UserEntity> userEntity = userService.getUserFromId(userId);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userEntity,null,null);
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
        filterChain.doFilter(request,response);
    }
}
