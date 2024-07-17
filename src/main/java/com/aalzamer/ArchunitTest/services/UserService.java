package com.aalzamer.ArchunitTest.services;

import com.aalzamer.ArchunitTest.repos.UserRepo;
import com.aalzamer.ArchunitTest.models.UserModel;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public Optional<UserModel> findById(Long id){
        return userRepo.findById(id);
    }
    public Iterable<UserModel> findAll(){
        return userRepo.findAll();
    }
}
