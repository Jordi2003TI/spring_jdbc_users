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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ra2.users.com_ra2_users.Login.CustomerLogin;
import com.ra2.users.com_ra2_users.model.User;
import com.ra2.users.com_ra2_users.repository.UserRepository;

@Service
public class userService {
    
    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    CustomerLogin customerLogin;

    public List<User> getUser() throws IOException{
        
        List<User> users = userRepository.findAll();
        return users;
    }

    public User getOneUser(long user_id, boolean log) throws IOException{
        
        List<User> oneUser = userRepository.findOne(user_id);
        // Le pongo un boolean porque sino en el lg me lo imprime dos veces entoces cuando llamo al metodo le digo si quiero que entre en el log
        if(log){
            if(!oneUser.isEmpty()){
                customerLogin.info("userService", "getOneUser", "Consultando user con id " + user_id);
            }else{
                customerLogin.error("userService", "getOneUser", "User amb la id " + user_id + " no existeix Missatge d'" + ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error intern del servidor"));
                return null;
            }
        }
        return oneUser.get(0);
    }
    
    public int updateUserPut(long user_id, User user) throws IOException {
        int updateUser = userRepository.updateUser(user_id, user);

        customerLogin.info("userService", "updateUser", "Modificant user amb id: " + user_id);
        if(updateUser == 1){
            customerLogin.info("userService", "updateUser", "User modficat correctament");
        }else{
            customerLogin.error("userService", "updateUser", "User amb id: " + user_id + " no existeix");
        }

        return updateUser;
    }

    public int addUser(User user) throws IOException {
        int result = userRepository.insertUser(user);
        customerLogin.info("userServide", "addUser", "Creant un user");
        if(result == 1){
            customerLogin.info("userServide", "addUser", "User creat correctament");
        }else{
            customerLogin.error("userServide", "addUser", "User amb nom: " + user.getNom() + " no s'ha creat correctament");
        }

        return result;
    }

    public int updateUserPatch(long user_id, String name, boolean log) throws IOException {
        int updated = userRepository.updateUserPatch(user_id, name);

        customerLogin.info("userService", "updateUserPatch", "Modificant user amb id: " + user_id);
        if(log){
            if(updated == 1){
                customerLogin.info("userService", "updateUserPatch", "User modficat correctament");
            }else{
                customerLogin.error("userService", "updateUserPatch", "User amb id: " + user_id + " no existeix");
            }
        }
        return updated;
    }

    public int deleteUser(long user_id, boolean log) throws IOException{
        int deletedUser = userRepository.deleteUser(user_id);

        customerLogin.info("userService", "deleteUser", "Borrant user amb id: " + user_id);
        if(log){
            if(deletedUser == 1){
                customerLogin.info("userService", "deleteUser", "El user amb id: " + user_id + " s'ha borrat correctament");
            }else{
                customerLogin.error("userService", "deleteUser", "El user amb id: " + user_id + " no existeix");
            }
        }
        return deletedUser;
    }

     public String uploadImage(long user_id, MultipartFile imageFile) throws IOException {
        List<User> user = userRepository.findOne(user_id);
        customerLogin.info("userService", "uploadImage", "Afegint la imatge " + imageFile.getName() + " per el user con la id: " + user_id);
        if (user.isEmpty()) {
            customerLogin.error("userService", "uploadImage", "User amb id " + user_id + " no existeix");
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

            customerLogin.info("userService", "uploadImage", "La imatge s'ha guardat correctament. El path és: /private/" + relativePath);
            return "/private/" + relativePath;
        } catch(Exception e){
            e.printStackTrace();
     }
        return null;
    }

    public String uploadCsv (MultipartFile csv) throws IOException{
        List<String> noAceptados = new ArrayList<>();
        int contarLinea = 1;
        
        int inserciones = 0;

        try(BufferedReader br = new BufferedReader(new InputStreamReader(csv.getInputStream()))){
            // sacamos la capcelera
            String linea = br.readLine();
            
            if(linea == null){
                return null;
            }
            customerLogin.info("userService", "uploadCsv", "Carregant la informacio del fitxer " + csv.getName());
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
                contarLinea++;
            }
        }catch(Exception e){
            customerLogin.error("userService", "uploadCsv", "Error en la linea " + contarLinea + " del fitxer.  Missatge d'error: " + e);
        }
        customerLogin.info("userService", "uploadCsv", "S'han guardat correctament " + inserciones + " i hsn donat error " + noAceptados.size() + " registres");

        return "Inserciones hechas: " + inserciones + " Y no hechas " + noAceptados.size();
    }
    // Para crear ususarios a partir de un json
    public int PostJson(MultipartFile json) throws IOException{
        int contadorInsertados = 0;
        int contadorErrores = 0;
        int lineas = 0;
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
                // Creamos o guardamos la carpeta de destino 
                Path carpeta = Paths.get("json_processed");
                if(!Files.exists(carpeta)){
                    Files.createDirectories(carpeta);
                }

                // Guardamos el destino
                Path lugarDestino = carpeta.resolve(json.getOriginalFilename());
                Files.copy(json.getInputStream(), lugarDestino, StandardCopyOption.REPLACE_EXISTING);
                lineas++;
                // Cogemos los nombres de los usuarios
                String name = user.path("name").asText();
                // Cogemos las descripciones
                String description = user.path("description").asText();
                // Cogemos el email
                String email = user.path("email").asText();
                // Cogemos las passwords
                String password = user.path("password").asText();

                customerLogin.info("userService", "PostJson", "Carregant la informacio del fitxer " + json.getName());
                User userSave = new User(name, description, email, password, LocalDateTime.now(), LocalDateTime.now());
                int insertados = userRepository.insertUser(userSave);
                if(insertados == 1){
                    contadorInsertados++;
                }
  

            }
        }catch(Exception e){
            contadorErrores++;
            customerLogin.error("userService", "PostJson", "Error en la linea " + lineas + ":" + e);
        }

        customerLogin.info("userService", "PostJson", "S'han guardar correctament " + contadorInsertados + " y han dado error " + contadorErrores);
        return contadorInsertados;
        
    }

    // try(BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream())))

}

