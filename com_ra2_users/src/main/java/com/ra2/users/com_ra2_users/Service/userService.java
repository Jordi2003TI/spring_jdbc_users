package com.ra2.users.com_ra2_users.Service;

import java.util.List;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ra2.users.com_ra2_users.model.User;
import com.ra2.users.com_ra2_users.repository.UserRepository;

@Service
public class userService {
    
    @Autowired
    UserRepository userRepository;

    public List<User> getUser(){
        List<User> users = userRepository.findAll();
        return users;
    }

    public User getOneUser(long user_id){
        List<User> oneUser = userRepository.findOne(user_id);
        return oneUser.get(0);
    }
    
    public int updateUserPut(long user_id, User user) {
        int updateUser = userRepository.updateUser(user_id, user);
        return updateUser;
    }

    public int addUser(User user) {
        int result = userRepository.insertUser(user);
        return result;
    }

    public int updateUserPatch(long user_id, String name) {
        int updated = userRepository.updateUserPatch(user_id, name);
        return updated;
    }

    public int deleteUser(long user_id){
        int deletedUser = userRepository.deleteUser(user_id);
        return deletedUser;
    }

     public String uploadImage(long user_id, MultipartFile imageFile) throws IOException {
        List<User> user = userRepository.findOne(user_id);
        if (user.isEmpty()) {
            return null;
        }
        try{
        // creamos el path donde queremos que cree la carpeta 
        Path imageDir = Paths.get("src/main/resources/public/images");
        // Comprobamos que exista 
        if (!Files.exists(imageDir)) {
            Files.createDirectories(imageDir);
        }
        // Creamos un nombre distinto para cada imagen 
        String filename = "user_" + user_id + "_" + imageFile.getOriginalFilename();
        Path destination = imageDir.resolve(filename);
        // hacemos el nio2 el inputstream es para recuperar el binario de la imagen 
        InputStream inputStream = imageFile.getInputStream();
        
        Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
        
        // creamos una ruta relativa para guardar en la base de datos
        String relativePath = "images/" + filename;
        userRepository.uploadImage(user_id, relativePath);
        return "/public/" + relativePath;
    } catch(Exception e){
        e.printStackTrace();
    }
        return null;
    }

}

