package com.mydocs.mydocs.springSecurity;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.mydocs.mydocs.springSecurity.authDto.AuthDto;
import com.mydocs.mydocs.util.ResponseHandler;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AuthController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

     @Autowired
    AuthenticationService authenticationService;
 
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDto request,HttpServletResponse response) {
       System.out.println("in mapping");
        return ResponseHandler.generateResponse(HttpStatus.OK,true,"Login successfull",authenticationService.login(request,response));
    }
 
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody AuthDto request,HttpServletResponse response) {
       
        return ResponseHandler.generateResponse(HttpStatus.OK,true,"Access Token generated",authenticationService.refreshToken(request,response));
      
    }    
}
