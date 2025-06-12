package com.mydocs.mydocs.Repositories.userRepositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mydocs.mydocs.domain.userDomain.Users;
import java.util.List;


public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByEmail(String email);
}
