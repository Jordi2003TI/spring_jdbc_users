package com.ra2.users.com_ra2_users.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ra2.users.com_ra2_users.Service.userService;
import com.ra2.users.com_ra2_users.model.User;
import com.ra2.users.com_ra2_users.repository.UserRepository;

import jakarta.websocket.server.PathParam;

import java.io.IOException;
import java.util.List;


import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;




@RestController
@RequestMapping("/api")
public class userController {

    @Autowired
    userService userServices;
    // Es para poder leer todos los usuarios que tenemos en la base de datos ne caso que no haya ninguno nos devolvera un null

        @GetMapping("/user")
        public ResponseEntity<List<User>> getUser() throws IOException {
        List<User> users = userServices.getUser();
        if(users.size() == 0){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(users);
        }
        
    }

    @GetMapping("user/{user_id}") // la variable tiene que ser igual a la que pasamos
    public ResponseEntity<User> getOneUser(@PathVariable long user_id) throws IOException{
        User userOne = userServices.getOneUser(user_id, true);
        
        if(userOne == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
         return ResponseEntity.status(HttpStatus.OK).body(userOne);// hacemos que nos devuleva el primero de la lista para que pueda devolver un tipo User sino daria error ya que estariamos devolviendo un tipo List
    }


    // actualizamos uno de los usuarios a parti de la id y un User Json
    @PutMapping("user/{user_id}")
    public ResponseEntity<User> updateUserPut(@PathVariable long user_id, @RequestBody User user) throws IOException {
        int updateUser = userServices.updateUserPut(user_id, user);
        if(updateUser == 0){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        User usuarioActualizado = userServices.getOneUser(user_id, false);
        return ResponseEntity.status(HttpStatus.OK).body(usuarioActualizado);
    }

    // crear un usuario Hacemos que nos devuelva un Responsitive porque queremos que nos devuelva una respuesta HTTP basicamente devolvemos un estado + un mensaje 
    @PostMapping("/user")
    public ResponseEntity<String> addUser(@RequestBody User user) throws IOException {
        int result = userServices.addUser(user);
        
        if(result > 0){
            return ResponseEntity.status(HttpStatus.OK).body("Usuario inserido correctamente " + user.getNom().toString());
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al inserir el usuario");
        }

    }
    // modificar un usario pero de 1 solo parametro 
    @PatchMapping("user/{user_id}/name")
    public ResponseEntity<String> updateUserPatch(@PathVariable() long user_id,@RequestParam() String name) throws IOException {

        if(name.length() > 100){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("El nombre no puede tener mas de 100 caracteres");
        }

        int updated = userServices.updateUserPatch(user_id, name, true);

        if(updated == 0){
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("El usuario con la id " + user_id + " no se ha econtrado");
        }

        User usuarioActualizado = userServices.getOneUser(user_id, false); 
        return ResponseEntity.status(HttpStatus.OK).body("Actualizado correctamente nom" + usuarioActualizado.getNom());
    }


    // borra un usario
    @DeleteMapping("/user/{user_id}")
    public ResponseEntity<String> deleteUser(@PathVariable long user_id) throws IOException{
        // Guardamos antes para porder imprimer que fue eliminado
        User userEontrado = userServices.getOneUser(user_id, false);

        int deletedUser = userServices.deleteUser(user_id, true);
        if(deletedUser >= 1){
            return ResponseEntity.status(HttpStatus.OK).body("Eliminado correctamente " + userEontrado);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("El usuario con la " + user_id + " no fue encontrado");
    }
     
    // para poder subir fotos postear digamos mejor 
    @PostMapping(value = "user/{user_id}/image")
    public ResponseEntity<String> addImage(@PathVariable long user_id, @RequestParam MultipartFile image) throws IOException {
        String resultado = userServices.uploadImage(user_id, image);
        return ResponseEntity.status(HttpStatus.OK).body(resultado);
    }
    // Subimos un csv para importar a la base de datos
    @PostMapping("user/csv")
    public ResponseEntity<String> addCsv(@RequestParam MultipartFile csv)throws IOException {
        String resultado = userServices.uploadCsv(csv);
        return ResponseEntity.status(HttpStatus.OK).body(resultado);
    }
    
    
    @PostMapping("users/upload-json")
    public ResponseEntity<String> PostJson(@RequestParam MultipartFile json) {
        int resultado = userServices.PostJson(json);

        if(resultado == 0){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No se completo la operacion");
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Usuarios agregados correctamente " + resultado);
        }
    }
     
    
}
