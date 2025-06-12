package com.mydocs.mydocs.services.userServices;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.mydocs.mydocs.domain.userDomain.Users;
import com.mydocs.mydocs.userDto.UserDto;

public interface userService extends UserDetailsService {
    
    public Users createUser(UserDto userDto);

    public List<Users> getAllUsers();
}
