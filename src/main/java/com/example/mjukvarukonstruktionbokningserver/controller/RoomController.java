package com.example.mjukvarukonstruktionbokningserver.controller;

import com.example.mjukvarukonstruktionbokningserver.model.Booking;
import com.example.mjukvarukonstruktionbokningserver.model.Room;
import com.example.mjukvarukonstruktionbokningserver.model.TimeInterval;
import com.example.mjukvarukonstruktionbokningserver.repository.BookingRepository;
import com.example.mjukvarukonstruktionbokningserver.repository.RoomRepository;
import com.example.mjukvarukonstruktionbokningserver.repository.TimeIntervalRepository;
import com.example.mjukvarukonstruktionbokningserver.viewmodel.RoomViewModel;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/")
public class RoomController {

    @Autowired
    RoomRepository roomRepository;
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    TimeIntervalRepository timeIntervalRepository;

    // Get All Rooms
    @GetMapping("/admin/listrooms")
    public List<RoomViewModel> getAllRooms() {
        return convertToViewModel(roomRepository.findAll());
    }

    // Create a new Room
    @PostMapping("/admin/addrooms")
    public Room createRoom(@Valid @RequestBody Room room) {
        Room r = roomRepository.save(room);
        return r;
    }

    @GetMapping("/admin/rooms/{room_name}")
    public ResponseEntity<Room> getRoomByRoomname(@PathVariable(value = "room_name") int room_name) {
        Room room = roomRepository.findOne(room_name);
        if(room == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(room);
    }

    @GetMapping("/admin/rooms/floors/{floor}")
    public List<RoomViewModel> getRoomByFloor(@PathVariable(value = "floor") int floor) {
        List<Room> rooms = roomRepository.findByFloor(floor);
        List<RoomViewModel> roomViewModels = convertToViewModel(rooms);
        return roomViewModels;
    }

    // Get a Single Room
    @GetMapping("/rooms/{date}/{startTime}")
    public List<RoomViewModel> getAvailableRooms(@PathVariable(value = "date") String date, @PathVariable(value = "startTime") float starttime) {

        deleteIfNecesseary(date);

        List<Room> rooms = roomRepository.findAll();
        List<Booking> bookings = bookingRepository.findBookingByDateAndStartTime(date, starttime);

        for(int i = 0;i<rooms.size();i++) {
            for(int j=0;j<bookings.size();j++) {
                if(rooms.get(i).getRoomName().equals(bookings.get(j).getRoomname()))
                    rooms.remove(i);
            }
        }

        List<RoomViewModel> roomViewModels = convertToViewModel(rooms);

        return roomViewModels;
    }

    // Update a Room
    @PutMapping("/admin/updaterooms/{roomname}")
    public ResponseEntity<RoomViewModel> updateRoom(@PathVariable(value = "roomname") String roomname,
                                           @Valid @RequestBody RoomViewModel roomDetails) {
        Room room = roomRepository.findByRoomName(roomname);
        if(room == null) {
            return ResponseEntity.notFound().build();
        }
        room.setRoomName(roomDetails.getRoomName());
        room.setTYPE(roomDetails.getTYPE());
        room.setBookableKTH(roomDetails.isBookableKTH());
        room.setBookableRKH(roomDetails.isBookableRKH());
        room.setCapacity(roomDetails.getCapacity());
        room.setFloor(roomDetails.getFloor());
        room.setQRcode(roomDetails.getQRcode());


        Room updatedroom = roomRepository.save(room);
        RoomViewModel rvm = new RoomViewModel(updatedroom.getRoomName(), updatedroom.getTYPE(), updatedroom.getQRcode(), updatedroom.getFloor(), updatedroom.getCapacity(), updatedroom.isBookableKTH(), updatedroom.isBookableRKH());

        return ResponseEntity.ok(rvm);
    }

    // Delete a Room
    @DeleteMapping("/admin/deleterooms/{roomname}")
    public ResponseEntity<RoomViewModel> deleteRoom(@PathVariable(value = "roomname") String roomname) {
        Room room = roomRepository.findByRoomName(roomname);
        if(room == null) {
            return ResponseEntity.notFound().build();
        }

        roomRepository.delete(room);
        return ResponseEntity.ok().build();
    }

    // Get a Single Room
    @GetMapping("/rooms/floors")
    public List<Integer> getNumberOfFloors() {
        List<Integer> floors = roomRepository.findByFloor();
        return floors;
    }

    private void deleteIfNecesseary(String date){
        List<TimeInterval> timeIntervals = timeIntervalRepository.findAll();
        List<String> starttimes = new ArrayList<>();
        List<String> endtimes = new ArrayList<>();

        for(int i = 0; i < timeIntervals.size(); i++) {
            String tmpstart = Float.toString(timeIntervals.get(i).getStartTime());
            String[] tmparraystart = tmpstart.split("\\.");
            String tmpend = Float.toString(timeIntervals.get(i).getStopTime());
            String[] tmparrayend = tmpend.split("\\.");
            starttimes.add(tmparraystart[0]);
            endtimes.add(tmparrayend[0]);
        }

        Date rightnow = Calendar.getInstance().getTime();

        SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
        String timenow = parser.format(rightnow);

        try {
            Date userDate = parser.parse(timenow);
            for(int i = 0; i < starttimes.size(); i++) {
                if (userDate.after(parser.parse(starttimes.get(i) + ":15")) && userDate.before(parser.parse(endtimes.get(i) + ":00"))) {
                    bookingRepository.removeBookingByDateAndStartTimeAndCheckedInFalseOrSecondaryCheckInFalse(date, Float.parseFloat(starttimes.get(i)));
                }
                if(userDate.after(parser.parse(endtimes.get(i) + ":00"))) {
                    bookingRepository.removeBookingByDateAndEndTime(date, Float.parseFloat(endtimes.get(i)));
                }
            }

            bookingRepository.removeAllBookingByDateBefore(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private List<RoomViewModel> convertToViewModel(List<Room> rooms) {

        List<RoomViewModel> rmv = new ArrayList<>();

        for(int i = 0; i<rooms.size();i++) {
            rmv.add(new RoomViewModel(rooms.get(i).getRoomName(), rooms.get(i).getTYPE(), rooms.get(i).getQRcode(), rooms.get(i).getFloor(), rooms.get(i).getCapacity(), rooms.get(i).isBookableKTH(), rooms.get(i).isBookableRKH()));
        }
        return rmv;
    }

}