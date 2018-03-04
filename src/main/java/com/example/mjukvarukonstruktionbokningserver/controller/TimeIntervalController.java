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
    @GetMapping("/timeInterval/getsingle/{id}")
    public ResponseEntity<TimeIntervalViewModel> getNoteById(@PathVariable(value = "id") int timeIntervalId) {
        TimeInterval timeInterval = timeIntervalRepository.findOne(timeIntervalId);
        if(timeInterval == null) {
            return ResponseEntity.notFound().build();
        }

        TimeIntervalViewModel timeIntervalViewModel = new TimeIntervalViewModel(timeInterval.getStartTime(), timeInterval.getStopTime());

        return ResponseEntity.ok().body(timeIntervalViewModel);
    }

    // Update a Room
    @PutMapping("/timeInterval/update/{id}")
    public ResponseEntity<TimeIntervalViewModel> updateNote(@PathVariable(value = "id") int timeIntervalId,
                                           @Valid @RequestBody TimeIntervalViewModel timeIntervalDetails) {
        TimeInterval timeInterval = timeIntervalRepository.findOne(timeIntervalId);
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
    @DeleteMapping("/timeInterval/delete/{id}")
    public ResponseEntity<TimeIntervalViewModel> deleteNote(@PathVariable(value = "id") int timeIntervalId) {
        TimeInterval timeInterval = timeIntervalRepository.findOne(timeIntervalId);
        if(timeInterval == null) {
            return ResponseEntity.notFound().build();
        }

        timeIntervalRepository.delete(timeInterval);

        return ResponseEntity.ok().build();
    }

    private List<TimeIntervalViewModel> convertToViewModel(List<TimeInterval> timeIntervals) {

        List<TimeIntervalViewModel> timv = new ArrayList<>();

        for(int i = 0; i<timeIntervals.size();i++) {
            timv.add(new TimeIntervalViewModel(timeIntervals.get(i).getStartTime(), timeIntervals.get(i).getStopTime()));
        }
        return timv;
    }
}