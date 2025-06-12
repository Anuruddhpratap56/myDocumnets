package com.mydocs.mydocs.controller.userController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import com.mydocs.mydocs.domain.userDomain.Users;
import com.mydocs.mydocs.services.userServices.userService;
import com.mydocs.mydocs.userDto.UserDto;
import com.mydocs.mydocs.util.ResponseHandler;
import com.mydocs.mydocs.util.UrlMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class UserController {

    @Autowired
    userService userService;

    @PostMapping(value = UrlMapping.CREATE_USER)
    public ResponseEntity<Object> createUser(@RequestBody UserDto userDto) {
        
        Users user = userService.createUser(userDto);
        
        return ResponseHandler.generateResponse(HttpStatus.OK,true,"UserCreated",user);
    
    }

    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(UrlMapping.GET_ALL_USERS)
    public ResponseEntity<Object> getAllUsers() {
        System.out.println("get All users apiu");
        List<Users> users = userService.getAllUsers();
        
        return ResponseHandler.generateResponse(HttpStatus.OK,true,"allUsersFetched",users);
    
    }
    

}

