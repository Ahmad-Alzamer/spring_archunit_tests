package com.aalzamer.ArchunitTest.controllers;

import com.aalzamer.ArchunitTest.models.UserModel;
import com.aalzamer.ArchunitTest.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/v1")
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("users/{id}")
    public ResponseEntity<UserModel> findUser(@PathVariable long id){
        log.info("received request");
        try{
            return userService.findById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }finally {
            log.info("completed request");
        }
    }
    @GetMapping("users")
    public ResponseEntity<Iterable<UserModel>> getAll(){
        log.info("received request");
        try{
            return ResponseEntity.ok(userService.findAll());
        }finally {
            log.info("completed request");
        }
    }
}
