package com.mydocs.mydocs.springSecurity;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.mydocs.mydocs.springSecurity.authDto.AuthDto;
import com.mydocs.mydocs.util.ResponseHandler;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public Map<String, Object> login(AuthDto loginRequest, HttpServletResponse response) {
        System.out.println("in login page");
        // Create authentication token
        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword());

        // Perform authentication
        Authentication authentication = authenticationManager.authenticate(authRequest);

        // Set authentication in security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate tokens
        String accessToken = jwtTokenUtil.generateAccessToken(loginRequest.getUserName());
        String refreshToken = jwtTokenUtil.generateRefreshToken(loginRequest.getUserName());

        // Set JWT as cookie
        Cookie jwtCookie = new Cookie("JWT", accessToken);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(24 * 60 * 60);
        response.addCookie(jwtCookie);

        // Prepare response data
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Map<String, Object> result = new HashMap<>();
        result.put("userName", userDetails.getUsername());
        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);

        return result;
    }

    public String refreshToken(AuthDto request,HttpServletResponse response) {
        String refreshToken = request.getRefreshToken();
        if (jwtTokenUtil.validateToken(refreshToken)) {
            String username = jwtTokenUtil.getUsernameFromToken(refreshToken);
            String newAccessToken = jwtTokenUtil.generateAccessToken(username);

            Cookie jwtCookie = new Cookie("JWT",newAccessToken);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(24*60*60);
            response.addCookie(jwtCookie);

            return newAccessToken;            

        }
        return null;            
    }   
}
