package com.example.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/registration")
public class UserRegistrationController {

    @PostMapping("/user")
    public String registerUser(@RequestBody Map<String, Object> formData) {
        System.out.println("Received data: " + formData);
        return "OK";
    }

}
