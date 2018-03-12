package com.example.mjukvarukonstruktionbokningserver.controller;

import com.example.mjukvarukonstruktionbokningserver.model.AdminSettings;
import com.example.mjukvarukonstruktionbokningserver.model.TimeInterval;
import com.example.mjukvarukonstruktionbokningserver.model.User;
import com.example.mjukvarukonstruktionbokningserver.repository.AdminSettingsRepository;
import com.example.mjukvarukonstruktionbokningserver.repository.UserRepository;
import com.example.mjukvarukonstruktionbokningserver.viewmodel.AdminSettingsViewModel;
import com.example.mjukvarukonstruktionbokningserver.viewmodel.TimeIntervalViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class AdminSettingsController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    AdminSettingsRepository adminSettingsRepository;

    // Create a new Room
    @PostMapping("/admin/adminsettings/setmaxhour")
    public boolean createMaxHour(@Valid @RequestBody AdminSettings adminSettings) {
        List<AdminSettings> isexisting = adminSettingsRepository.findAll();
        if(!isexisting.isEmpty())
            return false;

        AdminSettings a = adminSettingsRepository.save(adminSettings);
        return true;
    }

    @PutMapping("/admin/adminsettings/addadmin/{username}/")
    public boolean addAdmin(@PathVariable(value = "username") String username) {
        User user = userRepository.findByUserName(username);
        if(user == null)
            return false;

        user.setAdmin(true);
        User u = userRepository.save(user);
        return true;
    }

    @PutMapping("/admin/adminsettings/removeadmin/{username}/")
    public boolean removeAdmin(@PathVariable(value = "username") String username) {
        User user = userRepository.findByUserName(username);
        if(user == null)
            return false;

        user.setAdmin(false);
        User u = userRepository.save(user);
        return true;
    }

    // Get All Rooms
    @GetMapping("/admin/adminsettings/getmaxhour")
    public List<AdminSettingsViewModel> getAllMaxhours() {
        List<AdminSettingsViewModel> adminSettingsViewModels = convertToViewModel(adminSettingsRepository.findAll());
        if(adminSettingsViewModels.isEmpty()) {
            return new ArrayList<>();
        }
        return adminSettingsViewModels;
    }

    @PutMapping("/admin/adminsettings/changemaxtime/{maxhours}/")
    public boolean updateMaxhours(@PathVariable(value = "maxhours") int newmaxhours) {
        List<User> allusers = userRepository.findAll();

        if(allusers.isEmpty()) {
            return false;
        }

        List<AdminSettings> adminSettings = adminSettingsRepository.findAll();
        if(adminSettings.isEmpty())
            return false;

        if(adminSettings.size() > 1) {
            return false;
        }

        int currenthours = adminSettings.get(0).getMaxhours();

        int first = 0;
        int second = 0;
        int third = 0;

        if(currenthours < newmaxhours) {
            for(User user: allusers) {
                first = user.getFirsthours();
                second = user.getSecondhours();
                third = user.getThirdhours();

                user.setFirsthours(first + (newmaxhours - currenthours));
                user.setSecondhours(second + (newmaxhours - currenthours));
                user.setThirdhours(third + (newmaxhours - currenthours));
            }

        }

        adminSettings.get(0).setMaxhours(newmaxhours);

        adminSettingsRepository.save(adminSettings);

        return true;
    }

    @DeleteMapping("admin/adminsettings/deletemaxhour/{maxhour}/")
    public boolean deleteMaxhour(@PathVariable(value = "maxhours") int deletemaxhours) {
        AdminSettings adminSettings = adminSettingsRepository.findByMaxhours(deletemaxhours);
        if(adminSettings == null) {
            return false;
        }

        adminSettingsRepository.delete(adminSettings);

        return true;
    }

    private List<AdminSettingsViewModel> convertToViewModel(List<AdminSettings> adminSettings) {

        List<AdminSettingsViewModel> asmv = new ArrayList<>();

        for(int i = 0; i<adminSettings.size();i++) {
            asmv.add(new AdminSettingsViewModel(adminSettings.get(i).getMaxhours()));
        }
        return asmv;
    }

}
