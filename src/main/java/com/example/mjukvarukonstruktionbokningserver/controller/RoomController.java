package com.example.mjukvarukonstruktionbokningserver.controller;

import com.example.mjukvarukonstruktionbokningserver.model.Booking;
import com.example.mjukvarukonstruktionbokningserver.model.Room;
import com.example.mjukvarukonstruktionbokningserver.model.TimeInterval;
import com.example.mjukvarukonstruktionbokningserver.repository.BookingRepository;
import com.example.mjukvarukonstruktionbokningserver.repository.RoomRepository;
import com.example.mjukvarukonstruktionbokningserver.repository.TimeIntervalRepository;
import com.example.mjukvarukonstruktionbokningserver.viewmodel.BookingViewModel;
import com.example.mjukvarukonstruktionbokningserver.viewmodel.RoomViewModel;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
        List<Room> rooms = roomRepository.findAll();
        if(rooms.isEmpty())
            return new ArrayList<>();
        return convertToViewModel(rooms);
    }

    // Create a new Room
    @PostMapping("/admin/addrooms")
    public boolean createRoom(@Valid @RequestBody Room room) {

        Room isexisitingroom = roomRepository.findByRoomName(room.getRoomName());
        Room isexisitingqr = roomRepository.findByQrcode(room.getQrcode());

        if(isexisitingroom != null || isexisitingqr != null) {
            return false;
        }

        Room r = roomRepository.save(room);
        return true;
    }

    @GetMapping("/admin/rooms/{room_name}/")
    public ResponseEntity<Room> getRoomByRoomname(@PathVariable(value = "room_name") int room_name) {
        Room room = roomRepository.findOne(room_name);
        if(room == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(room);
    }

    @GetMapping("/admin/rooms/floors/{floor}/")
    public List<RoomViewModel> getRoomByFloor(@PathVariable(value = "floor") int floor) {
        List<Room> rooms = roomRepository.findByFloor(floor);
        List<RoomViewModel> roomViewModels = convertToViewModel(rooms);
        return roomViewModels;
    }

    // Get a Single Room
    @GetMapping("/rooms/{date}/{startTime}/")
    public List<RoomViewModel> getAvailableRooms(@PathVariable(value = "date") String datestring, @PathVariable(value = "startTime") float starttime) {

        if(!deleteIfNecesseary()) {
            return new ArrayList<RoomViewModel>();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(datestring);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<Room> rooms = roomRepository.findAll();
        if(rooms.isEmpty()) {
            return new ArrayList<RoomViewModel>();
        }
        List<Booking> bookings = bookingRepository.findBookingByDateAndStartTime(date, starttime);

        for(int i = 0;i<rooms.size();i++) {
            for(int j=0;j<bookings.size();j++) {
                    if (rooms.get(i).getRoomName().equals(bookings.get(j).getRoomname()))
                        rooms.remove(i);
            }
        }

        List<RoomViewModel> roomViewModels = convertToViewModel(rooms);

        return roomViewModels;
    }

    // Update a Room
    @PutMapping("/admin/updaterooms/{roomname}/")
    public boolean updateRoom(@PathVariable(value = "roomname") String roomname,
                                           @Valid @RequestBody Room roomDetails) {
        Room room = roomRepository.findByRoomName(roomname);
        if(room == null) {
            return false;
        }
        room.setRoomName(roomDetails.getRoomName());
        room.setTYPE(roomDetails.getTYPE());
        room.setBookableKTH(roomDetails.isBookableKTH());
        room.setBookableRKH(roomDetails.isBookableRKH());
        room.setCapacity(roomDetails.getCapacity());
        room.setFloor(roomDetails.getFloor());
        room.setQrcode(roomDetails.getQrcode());

        Room updatedroom = roomRepository.save(room);
        return true;
    }

    // Delete a Room
    @DeleteMapping("/admin/deleterooms/{roomname}/")
    public boolean deleteRoom(@PathVariable(value = "roomname") String roomname) {
        Room room = roomRepository.findByRoomName(roomname);
        if(room == null) {
            return false;
        }

        roomRepository.delete(room);
        return true;
    }

    // Get a Single Room
    @GetMapping("/rooms/floors")
    public List<Integer> getNumberOfFloors() {
        List<Integer> floors = roomRepository.findByFloor();
        return floors;
    }

    public boolean deleteIfNecesseary(){
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
        Date rnow = new Date();

        SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String dateNow = dateFormat.format(rnow);
        String timenow = parser.format(rightnow);

        try {
            Date userDate = parser.parse(timenow);
            Date now = dateFormat.parse(dateNow);
            for(int i = 0; i < starttimes.size(); i++) {
                if (userDate.after(parser.parse(starttimes.get(i) + ":15")) && userDate.before(parser.parse(endtimes.get(i) + ":00"))) {
                    bookingRepository.removeBookingByDateAndStartTimeAndCheckedEquals(now, Float.parseFloat(starttimes.get(i)), false);
                    bookingRepository.removeBookingByDateAndStartTimeAndSecondaryCheckedEquals(now, Float.parseFloat(starttimes.get(i)), false);
                }
                if(userDate.after(parser.parse(endtimes.get(i) + ":00"))) {
                    bookingRepository.removeBookingByDateAndEndTime(now, Float.parseFloat(endtimes.get(i)));
                }
            }

              bookingRepository.removeByDateBefore(now);

        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private List<RoomViewModel> convertToViewModel(List<Room> rooms) {

        List<RoomViewModel> rmv = new ArrayList<>();

        for(int i = 0; i<rooms.size();i++) {
            rmv.add(new RoomViewModel(rooms.get(i).getRoomName(), rooms.get(i).getTYPE(), rooms.get(i).getQrcode(), rooms.get(i).getFloor(), rooms.get(i).getCapacity(), rooms.get(i).isBookableKTH(), rooms.get(i).isBookableRKH()));
        }
        return rmv;
    }

}