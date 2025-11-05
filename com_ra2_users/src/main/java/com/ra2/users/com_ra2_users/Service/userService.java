package com.ra2.users.com_ra2_users.Service;

import java.util.List;

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

    public String uploadImage(long user_id, MultipartFile image){
        List<User> user = userRepository.findOne(user_id);
        User oneUser = user.get(0);

        if(oneUser != null){
            return"Si se econtro el usuario";
        }else{
            return"No existe el usuario";
        }
        
    }
}

