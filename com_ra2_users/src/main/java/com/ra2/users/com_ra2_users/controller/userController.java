package com.ra2.users.com_ra2_users.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ra2.users.com_ra2_users.model.User;
import com.ra2.users.com_ra2_users.repository.UserRepository;

import java.util.List;

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




@RestController
@RequestMapping("/api")
public class userController {

    @Autowired
    UserRepository userRepository;

    // Es para poder leer todos los usuarios que tenemos en la base de datos ne caso que no haya ninguno nos devolvera un null

        @GetMapping("/user")
        public ResponseEntity<List<User>> getUser() {
        List<User> users = userRepository.findAll();
        if(users.size() == 0){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(users);
        }
        
    }

    @GetMapping("user/{user_id}") // la variable tiene que ser igual a la que pasamos
    public ResponseEntity<User> getOneUser(@PathVariable long user_id){
        List<User> oneUser = userRepository.findOne(user_id);
        
        if(oneUser == null || oneUser.isEmpty()){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
         User user = oneUser.get(0);
         return ResponseEntity.status(HttpStatus.OK).body(user);// hacemos que nos devuleva el primero de la lista para que pueda devolver un tipo User sino daria error ya que estariamos devolviendo un tipo List
    }


    // actualizamos uno de los usuarios a parti de la id y un User Json
    @PutMapping("user/{user_id}")
    public ResponseEntity<User> updateUserPut(@PathVariable long user_id, @RequestBody User user) {
        int updateUser = userRepository.updateUser(user_id, user);
        if(updateUser == 0){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        User usuarioActualizado = userRepository.findOne(user_id).get(0);
        return ResponseEntity.status(HttpStatus.OK).body(usuarioActualizado);
    }

    // crear un usuario Hacemos que nos devuelva un Responsitive porque queremos que nos devuelva una respuesta HTTP basicamente devolvemos un estado + un mensaje 
    @PostMapping("/user")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        int result = userRepository.insertUser(user);
        
        if(result > 0){
            return ResponseEntity.status(HttpStatus.OK).body("Usuario inserido correctamente " + user.getNom().toString());
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al inserir el usuario");
        }

    }
    // modificar un usario pero de 1 solo parametro 
    @PatchMapping("user/{user_id}/name")
    public ResponseEntity<String> updateUserPatch(@PathVariable() long user_id,@RequestParam() String name) {

        if(name.length() > 100){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("El nombre no puede tener mas de 100 caracteres");
        }

        int updated = userRepository.updateUserPatch(user_id, name);

        if(updated == 0){
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("El usuario con la id " + user_id + " no se ha econtrado");
        }

        User usuarioActualizado = userRepository.findOne(user_id).get(0); 
        return ResponseEntity.status(HttpStatus.OK).body("Actualizado correctamente nom" + usuarioActualizado.getNom());
    }


    // borra un usario
    @DeleteMapping("/user/{user_id}")
    public ResponseEntity<String> deleteUser(@PathVariable long user_id){
        // Guardamos antes para porder imprimer que fue eliminado
        List<User> userEontrado = userRepository.findOne(user_id);

        int deletedUser = userRepository.deleteUser(user_id);
        if(deletedUser >= 1){
            return ResponseEntity.status(HttpStatus.OK).body("Eliminado correctamente " + userEontrado.get(0));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("El usuario con la " + user_id + " no fue encontrado");
    }
    
    
}
