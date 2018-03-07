package com.example.mjukvarukonstruktionbokningserver.controller;

import com.example.mjukvarukonstruktionbokningserver.model.Booking;
import com.example.mjukvarukonstruktionbokningserver.model.Room;
import com.example.mjukvarukonstruktionbokningserver.model.TimeInterval;
import com.example.mjukvarukonstruktionbokningserver.repository.RoomRepository;
import com.example.mjukvarukonstruktionbokningserver.repository.TimeIntervalRepository;
import com.example.mjukvarukonstruktionbokningserver.viewmodel.BookingViewModel;
import com.example.mjukvarukonstruktionbokningserver.viewmodel.TimeIntervalViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class TimeIntervalController {

    @Autowired
    TimeIntervalRepository timeIntervalRepository;

    // Get All Rooms
    @GetMapping("/timeInterval/getall")
    public List<TimeIntervalViewModel> getAllNotes() {
        List<TimeIntervalViewModel> timeIntervalViewModels = convertToViewModel(timeIntervalRepository.findAll());
        return timeIntervalViewModels;
    }

    // Create a new Room
    @PostMapping("/timeInterval/create")
    public TimeInterval createNote(@Valid @RequestBody TimeInterval timeInterval) {
        TimeInterval t = timeIntervalRepository.save(timeInterval);
        return t;
    }

    // Get a Single Room
    @GetMapping("/timeInterval/getsingle/{starttime}/{endtime}")
    public ResponseEntity<TimeIntervalViewModel> getNoteById(@PathVariable(value = "starttime") float starttime, @PathVariable(value = "endtime") float endtime) {
        TimeInterval timeInterval = timeIntervalRepository.findByStartTimeAndStopTime(starttime, endtime);
        if(timeInterval == null) {
            return ResponseEntity.notFound().build();
        }

        TimeIntervalViewModel timeIntervalViewModel = new TimeIntervalViewModel(timeInterval.getStartTime(), timeInterval.getStopTime());

        return ResponseEntity.ok().body(timeIntervalViewModel);
    }

    // Update a Room
    @PutMapping("/timeInterval/update/{starttime}/{endtime}")
    public ResponseEntity<TimeIntervalViewModel> updateNote(@PathVariable(value = "starttime") float starttime, @PathVariable(value = "endtime") float endtime,
                                           @Valid @RequestBody TimeInterval timeIntervalDetails) {
        TimeInterval timeInterval = timeIntervalRepository.findByStartTimeAndStopTime(starttime, endtime);
        if(timeInterval == null) {
            return ResponseEntity.notFound().build();
        }
        timeInterval.setStartTime(timeIntervalDetails.getStartTime());
        timeInterval.setStopTime(timeIntervalDetails.getStopTime());

        TimeInterval updatedtimeInterval = timeIntervalRepository.save(timeInterval);

        TimeIntervalViewModel timeIntervalViewModel = new TimeIntervalViewModel(updatedtimeInterval.getStartTime(), updatedtimeInterval.getStopTime());

        return ResponseEntity.ok(timeIntervalViewModel);
    }

    // Delete a Room
    @DeleteMapping("/timeInterval/delete/{starttime}/{endtime}")
    public boolean deleteNote(@PathVariable(value = "starttime") float starttime, @PathVariable(value = "endtime") float endtime) {
        TimeInterval timeInterval = timeIntervalRepository.findByStartTimeAndStopTime(starttime, endtime);
        if(timeInterval == null) {
            return false;

        }

        timeIntervalRepository.delete(timeInterval);

        return true;
    }

    private List<TimeIntervalViewModel> convertToViewModel(List<TimeInterval> timeIntervals) {

        List<TimeIntervalViewModel> timv = new ArrayList<>();

        for(int i = 0; i<timeIntervals.size();i++) {
            timv.add(new TimeIntervalViewModel(timeIntervals.get(i).getStartTime(), timeIntervals.get(i).getStopTime()));
        }
        return timv;
    }
}