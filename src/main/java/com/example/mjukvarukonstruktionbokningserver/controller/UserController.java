package com.example.mjukvarukonstruktionbokningserver.controller;

import com.example.mjukvarukonstruktionbokningserver.model.AdminSettings;
import com.example.mjukvarukonstruktionbokningserver.model.Booking;
import com.example.mjukvarukonstruktionbokningserver.model.User;
import com.example.mjukvarukonstruktionbokningserver.repository.AdminSettingsRepository;
import com.example.mjukvarukonstruktionbokningserver.repository.UserRepository;
import com.example.mjukvarukonstruktionbokningserver.viewmodel.UserViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    AdminSettingsRepository adminSettingsRepository;

    @GetMapping("/admin/listallusers")
    public List<UserViewModel> getAllUsers() {
        return convertToViewModel(userRepository.findAll());
    }

    // Create a new User
    @PostMapping("/user/login/create")
    public ResponseEntity<UserViewModel> createUser(@Valid @RequestBody User user) {
        User isexisting = userRepository.findByUserName(user.getUserName());

        if(isexisting != null) {
            UserViewModel userViewModel = new UserViewModel(isexisting.getUserName(), isexisting.getAffiliation(), isexisting.isAdmin(), isexisting.getFirsthours(), isexisting.getSecondhours(), isexisting.getThirdhours());
            return ResponseEntity.ok().body(userViewModel);
        }

        List<AdminSettings> adminSettings = adminSettingsRepository.findAll();
        if(adminSettings.isEmpty())
            return ResponseEntity.notFound().build();

        if(adminSettings.size() > 1) {
            return ResponseEntity.notFound().build();
        }

        int maxhours = adminSettings.get(0).getMaxhours();

        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String d = df.format(date);
        try {
            date = df.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        user.setCurrentweek(cal.get(Calendar.WEEK_OF_YEAR));

        user.setFirsthours(maxhours);
        user.setSecondhours(maxhours);
        user.setThirdhours(maxhours);

        User u = userRepository.save(user);
        UserViewModel userViewModel = new UserViewModel(u.getUserName(), u.getAffiliation(), u.isAdmin(), u.getFirsthours(), u.getSecondhours(), u.getThirdhours());
        return ResponseEntity.ok().body(userViewModel);
    }

    // Get a Single User
    @GetMapping("/admin/user/{user_name}")
    public ResponseEntity<UserViewModel> getBookingById(@PathVariable(value = "user_name") String user_name) {
        User user = userRepository.findByUserName(user_name);
        if(user == null) {
            return ResponseEntity.notFound().build();
        }
        UserViewModel userViewModel = new UserViewModel(user.getUserName(), user.getAffiliation(), user.isAdmin(), user.getFirsthours(), user.getSecondhours(), user.getThirdhours());
        return ResponseEntity.ok().body(userViewModel);
    }

    // Update a Booking
    @PutMapping("/admin/user/adminpreferences/{user_name}")
    public boolean updateUser(@PathVariable(value = "user_name") String user_name,
                                                          @Valid @RequestBody User userDetails) {
        User user = userRepository.findByUserName(user_name);
        if(user == null) {
            return false;
        }

        user.setUserName(userDetails.getUserName());
        user.setAffiliation(userDetails.getAffiliation());
        user.setAdmin(userDetails.isAdmin());
        user.setFirsthours(userDetails.getFirsthours());
        user.setSecondhours(userDetails.getSecondhours());
        user.setThirdhours(userDetails.getThirdhours());

        User updateduser = userRepository.save(user);
        UserViewModel userViewModel = new UserViewModel(updateduser.getUserName(), updateduser.getAffiliation(), updateduser.isAdmin(), updateduser.getFirsthours(), updateduser.getSecondhours(), updateduser.getThirdhours());
        return true;
    }

    private List<UserViewModel> convertToViewModel(List<User> users) {

        List<UserViewModel> umv = new ArrayList<>();

        for(int i = 0; i<users.size();i++) {
            umv.add(new UserViewModel(users.get(i).getUserName(), users.get(i).getAffiliation(), users.get(i).isAdmin(), users.get(i).getFirsthours(), users.get(i).getSecondhours(), users.get(i).getThirdhours()));
        }
        return umv;
    }

}
