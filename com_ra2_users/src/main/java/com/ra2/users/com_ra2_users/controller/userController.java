package com.ra2.users.com_ra2_users.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ra2.users.com_ra2_users.model.User;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api")
public class userController {
    // Es para poder leer
    @GetMapping("/user")
    public String getUser() {
        return "Devuelvo un Usuario";
    }

    // crear un usuario
    @PostMapping("/user")
    public String addUser(@RequestBody User user) {
        return "He creado un usuario " + user.toString();
    }
    // modificar un usario
    @PatchMapping("/user")
    public String updateUser(){
        return "he actualiazado un usuario";
    }
    // borra un usario
    @DeleteMapping("/user")
    public String deleteUser(){
        return"He borrado un usuraio";
    }
    
    
}
