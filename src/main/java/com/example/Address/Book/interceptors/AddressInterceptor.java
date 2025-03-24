package com.example.Address.Book.interceptors;

import com.example.Address.Book.services.JwtTokenService;
import com.example.Address.Book.services.RedisTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AddressInterceptor implements HandlerInterceptor {

    @Autowired
    JwtTokenService jwtTokenService;

    @Autowired
    RedisTokenService redisTokenService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Get token from cookies
        String token = null;
        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if (cookie.getName().equals("jwt")) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // If no token, reject request
        if (token == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // Decode token to get user ID
        String userId = jwtTokenService.decodeToken(token).toString();

        // Check Redis for the token
        String storedToken = redisTokenService.getToken(userId);
        if (storedToken == null || !storedToken.equals(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // Token is valid → Allow request
        return true;
    }
}