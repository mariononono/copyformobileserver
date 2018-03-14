package com.example.copyformobileserver.controller;

import com.example.copyformobileserver.model.User;
import com.example.copyformobileserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/{username}/getuser/")
    public ResponseEntity<User> getUserByUsername(@PathVariable(value = "username") String username) {
        User user = userRepository.findByUserName(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/{username}/login/{password}/")
    public String login(@PathVariable(value = "username") String username, @PathVariable(value = "password") String password) {
        User user = userRepository.findByUserNameAndPassword(username, password);
        if (user == null) {
            return "fel";
        }

        return "rätt";
    }

    @GetMapping("/{username}/publickey/")
    public String getPublicKeyFromUser(@PathVariable(value = "username") String username) {
        User user = userRepository.findByUserName(username);
        if(user == null) {
            return null;
        }
        //PublicKey publicKey = user.getPublickey();
        return "";
    }

    @PostMapping("/user/register")
    public User register(@Valid @RequestBody User user) {
        User u = userRepository.save(user);
        return u;
    }

    @GetMapping("/user/search/{searchstring}")
    public List<String> getUsersByNameSearch(@PathVariable(value = "searchstring") String searchstring) {
        List<User> users = userRepository.findAllByUserName(searchstring);
        List<String> usernames = new ArrayList<>();

        for (User user : users) {
            usernames.add(user.getUserName());
        }
        return usernames;
    }

}
