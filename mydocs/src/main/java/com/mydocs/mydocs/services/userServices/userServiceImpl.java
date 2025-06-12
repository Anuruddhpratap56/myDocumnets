package com.mydocs.mydocs.services.userServices;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.mydocs.mydocs.domain.userDomain.Users;
import com.mydocs.mydocs.springSecurity.ByCryptPasswordEncoder;
import com.mydocs.mydocs.userDto.UserDto;
import com.mydocs.mydocs.Repositories.userRepositories.UserRepository;

@Service
public class userServiceImpl implements userService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ByCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userName){
        System.out.println("in load user by username");
        Users userData  = userRepository.findByEmail(userName);
        List<GrantedAuthority> authorities = userData.getRoles()
            .stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());        

        return new User(userData.getEmail(),userData.getPassword(), authorities);
    }
    @Override
    public Users createUser(UserDto userDto) {
        Users existingUser = userRepository.findByEmail(userDto.getEmail());
        if(existingUser!=null){
            return null;
        }

        Users user = new Users();

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setFullName(userDto.getFirstName()+" "+userDto.getLastName());
        user.setAge(userDto.getAge());
        user.setIsEnterprize(userDto.getIsEnterprize());
        user.setEmail(userDto.getEmail());
        user.setContactNumber(userDto.getContactNumber());
        String password = passwordEncoder.bCryptPasswordEncoder().encode(userDto.getPassword());
        user.setPassword(password);

        

        userRepository.save(user);

        return user;
    }


    // @Cacheable(value = "allUsers")
    public List<Users> getAllUsers(){
        return userRepository.findAll();
    }


}
