package com.example.mjukvarukonstruktionbokningserver.controller;

import com.example.mjukvarukonstruktionbokningserver.model.User;
import com.example.mjukvarukonstruktionbokningserver.repository.UserRepository;
import com.example.mjukvarukonstruktionbokningserver.viewmodel.UserViewModel;
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

    @GetMapping("/admin/listallusers")
    public List<UserViewModel> getAllUsers() {
        return convertToViewModel(userRepository.findAll());
    }

    // Create a new User
    @PostMapping("/user/login/create")
    public User createUser(@Valid @RequestBody User user) {
        User u = userRepository.save(user);
        return u;
    }

    // Get a Single User
    @GetMapping("/admin/user/{user_name}")
    public ResponseEntity<UserViewModel> getBookingById(@PathVariable(value = "user_name") String user_name) {
        User user = userRepository.findByUserName(user_name);
        if(user == null) {
            return ResponseEntity.notFound().build();
        }
        UserViewModel userViewModel = new UserViewModel(user.getUserName(), user.getAffiliation(), user.isAdmin(), user.getHours());
        return ResponseEntity.ok().body(userViewModel);
    }

    // Update a Booking
    @PutMapping("/admin/user/adminpreferences/{user_name}")
    public ResponseEntity<UserViewModel> updateUser(@PathVariable(value = "user_name") String user_name,
                                                          @Valid @RequestBody UserViewModel userDetails) {
        User user = userRepository.findByUserName(user_name);
        if(user == null) {
            return ResponseEntity.notFound().build();
        }

        user.setUserName(userDetails.getUsername());
        user.setAffiliation(userDetails.getAffiliation());
        user.setAdmin(userDetails.isAdmin());
        user.setHours(userDetails.getHours());

        User updateduser = userRepository.save(user);
        UserViewModel userViewModel = new UserViewModel(updateduser.getUserName(), updateduser.getAffiliation(), updateduser.isAdmin(), updateduser.getHours());
        return ResponseEntity.ok(userDetails);
    }

    private List<UserViewModel> convertToViewModel(List<User> users) {

        List<UserViewModel> umv = new ArrayList<>();

        for(int i = 0; i<users.size();i++) {
            umv.add(new UserViewModel(users.get(i).getUserName(), users.get(i).getAffiliation(), users.get(i).isAdmin(), users.get(i).getHours()));
        }
        return umv;
    }

}
