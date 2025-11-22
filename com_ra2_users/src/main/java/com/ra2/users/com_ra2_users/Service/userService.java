package com.ra2.users.com_ra2_users.Service;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ra2.users.com_ra2_users.model.User;
import com.ra2.users.com_ra2_users.repository.UserRepository;

@Service
public class userService {
    
    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper mapper;

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
        Path imageDir = Paths.get("private/images");
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

    public String uploadCsv (MultipartFile csv) throws IOException{
        List<String> noAceptados = new ArrayList<>();
        
        int inserciones = 0;

        try(BufferedReader br = new BufferedReader(new InputStreamReader(csv.getInputStream()))){
            // sacamos la capcelera
            String linea = br.readLine();
            
            if(linea == null){
                return null;
            }
            
            while((linea = br.readLine()) != null){
                String[] elemento = linea.split(",");
                if(elemento.length != 4){
                    noAceptados.add(elemento[0]);
                    continue;
                }

                User user = new User(elemento[0].trim(), elemento[1].trim(), elemento[2].trim(), elemento[3].trim(), LocalDateTime.now(), LocalDateTime.now());
                int confirmacion = userRepository.insertUser(user);
                if(confirmacion == 1){
                    inserciones++;
                }

            }
        }
        System.out.println(noAceptados);

        return "Inserciones hechas: " + inserciones + " Y no hechas " + noAceptados.size();
    }
    // Para crear ususarios a partir de un json
    public int PostJson(MultipartFile json){
        int contadorInsertados = 0;
        try{ // Siempre que trabajemos con un object de mapper tenemos que ponerlo dentro de un try catch
            JsonNode arrel = mapper.readTree(json.getInputStream());
            JsonNode data = arrel.path("data");
            int count = data.path("count").asInt();
            String control = data.path("control").asText();
            JsonNode users = data.path("users");
            // Comprobamos que tengamos users
            if(users == null || !users.isArray()){
                return 0;
            }

            for(JsonNode user : users){
                // Cogemos los nombres de los usuarios
                String name = user.path("name").asText();
                // Cogemos las descripciones
                String description = user.path("description").asText();
                // Cogemos el email
                String email = user.path("email").asText();
                // Cogemos las passwords
                String password = user.path("password").asText();

                User userSave = new User(name, description, email, password, LocalDateTime.now(), LocalDateTime.now());
                int insertados = userRepository.insertUser(userSave);
                if(insertados == 1){
                    contadorInsertados++;
                }

                // Creamos o guardamos la carpeta de destino 
                Path carpeta = Paths.get("json_processed");
                if(!Files.exists(carpeta)){
                    Files.createDirectories(carpeta);
                }

                // Guardamos el destino
                Path lugarDestino = carpeta.resolve(json.getOriginalFilename());
                Files.copy(json.getInputStream(), lugarDestino, StandardCopyOption.REPLACE_EXISTING);  

            }
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }
        return contadorInsertados;
        
    }

    // try(BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream())))

}

