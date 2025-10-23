package com.ra2.users.com_ra2_users.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ra2.users.com_ra2_users.model.User;
import com.ra2.users.com_ra2_users.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api")
public class userController {

    @Autowired
    UserRepository userRepository;

    // Es para poder leer
    @GetMapping("/user")
    public String getUser() {
        return "Devuelvo un Usuario";
    }

    // crear un usuario
    @PostMapping("/user")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        int result = userRepository.insertUser(user);
        
        if(result > 0){
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario inserido correctamente " + user.getNom().toString());
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al inserir el usuario");
        }

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
