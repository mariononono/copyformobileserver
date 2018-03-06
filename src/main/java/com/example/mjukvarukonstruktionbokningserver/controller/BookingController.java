package com.example.mjukvarukonstruktionbokningserver.controller;

import antlr.ASTNULLType;
import com.example.mjukvarukonstruktionbokningserver.model.Booking;
import com.example.mjukvarukonstruktionbokningserver.model.Room;
import com.example.mjukvarukonstruktionbokningserver.model.TimeInterval;
import com.example.mjukvarukonstruktionbokningserver.model.User;
import com.example.mjukvarukonstruktionbokningserver.repository.BookingRepository;
import com.example.mjukvarukonstruktionbokningserver.repository.RoomRepository;
import com.example.mjukvarukonstruktionbokningserver.repository.TimeIntervalRepository;
import com.example.mjukvarukonstruktionbokningserver.repository.UserRepository;
import com.example.mjukvarukonstruktionbokningserver.viewmodel.BookingViewModel;
import com.example.mjukvarukonstruktionbokningserver.viewmodel.RoomViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.awt.print.Book;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/")
public class BookingController {

    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TimeIntervalRepository timeIntervalRepository;


    // Get All bookings
    @GetMapping("/admin/bookings")
    public List<BookingViewModel> getAllBookings() {
        deleteIfNecesseary();
        return convertToViewModel(bookingRepository.findAll());
    }

    // Create a new Booking
    @PostMapping("/bookings/create")
    public Booking createBooking(@Valid @RequestBody Booking booking) {
        User user = userRepository.findByUserName(booking.getUserName());
        if(user != null) {
            if (user.isAdmin()) {
                booking.setCheckedIn(true);
                booking.setSecondaryCheckIn(true);
            }
        }
        Booking b = bookingRepository.save(booking);
        return b;
    }

    // Get a Single Room
    @GetMapping("/bookings/{username}/{date}/{starttime}/")
    public ResponseEntity<Booking> getBookingById(@PathVariable(value = "username") String username, @PathVariable(value = "date") String date, @PathVariable(value = "starttime") float starttime) {
        Booking booking = bookingRepository.findBookingByUserNameAndDateAndStartTimeEquals(username, date, starttime);
        if(booking == null) {
            booking = bookingRepository.findBookingBySecondaryUserNameAndDateAndStartTimeEquals(username, date, starttime);
        }
        if(booking == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(booking);
    }

    // Get a my bookings
    @GetMapping("/bookings/getbooking/{username}/")
    public List<BookingViewModel> getBookingByUsername(@PathVariable(value = "username") String username) {
        deleteIfNecesseary();
        List<BookingViewModel> bookingViewModels = convertToViewModel(bookingRepository.findByUserNameEquals(username));
        List<BookingViewModel> bookingViewModels1 = convertToViewModel(bookingRepository.findBySecondaryUserNameEquals(username));
        bookingViewModels.addAll(bookingViewModels1);
        return bookingViewModels;
    }

    // Update a Room
    @PutMapping("/bookings/checkin/{username}/{date}/{starttime}/{qr}/")
    public ResponseEntity<Booking> updateBooking(@PathVariable(value = "username") String username, @PathVariable(value = "date") String date, @PathVariable(value = "starttime") float starttime, @PathVariable(value = "qr") String qr) {
        Booking booking = bookingRepository.findBookingByUserNameAndDateAndStartTimeEquals(username, date, starttime);
        if(booking == null) {
            booking = bookingRepository.findBookingBySecondaryUserNameAndDateAndStartTimeEquals(username, date, starttime);
        }
        if(booking == null) {
            return ResponseEntity.notFound().build();
        }

        Room room = roomRepository.findByRoomName(booking.getRoomname());
        if(room == null) {
            return ResponseEntity.notFound().build();
        }

        if(username.equals(booking.getUserName()) && !booking.isCheckedIn()) {
            if(qr.equals(room.getQRcode()))
                booking.setCheckedIn(true);
        } else if(username.equals(booking.getSecondaryUserName()) && !booking.isSecondaryCheckIn()) {
            if(qr.equals(room.getQRcode()))
                booking.setSecondaryCheckIn(true);
        }

        Booking updatedbooking = bookingRepository.save(booking);
        return ResponseEntity.ok(updatedbooking);
    }

    // Update a Booking
    @PutMapping("/bookings/updatebooking/{username}/{date}/{starttime}/")
    public ResponseEntity<BookingViewModel> updateBooking(@PathVariable(value = "username") String username, @PathVariable(value = "date") String date, @PathVariable(value = "starttime") float starttime,
                                           @Valid @RequestBody BookingViewModel bookingDetails) {
        Booking booking = bookingRepository.findBookingByUserNameAndDateAndStartTimeEquals(username, date, starttime);
        if(booking == null) {
            return ResponseEntity.notFound().build();
        }
        booking.setUserName(bookingDetails.getUserName());
        booking.setStartTime(bookingDetails.getStartTime());
        booking.setRoomname(bookingDetails.getRoomname());
        booking.setDate(bookingDetails.getDate());
        booking.setEndTime(bookingDetails.getEndTime());
        booking.setSecondaryUserName(bookingDetails.getSecondaryUserName());


        Booking updatedbooking = bookingRepository.save(booking);
       // BookingViewModel bookingViewModel = new BookingViewModel(updatedbooking.getId(), updatedbooking.getUserName(), updatedbooking.getSecondaryUserName(), updatedbooking.getStartTime(), updatedbooking.getEndTime(), updatedbooking.getRoomname(), updatedbooking.getDate());
        return ResponseEntity.ok(bookingDetails);
    }

    @PutMapping("/bookings/updatebooking/confirm/{secondary_username}/{date}/{starttime}/")
    public ResponseEntity<BookingViewModel> confirmBooking(@PathVariable(value = "secondary_username") String secondaryUsername, @PathVariable(value = "date") String date, @PathVariable(value = "starttime") float starttime) {
        Booking booking = bookingRepository.findBookingBySecondaryUserNameAndDateAndStartTimeEquals(secondaryUsername, date, starttime);
        if(booking == null) {
            return ResponseEntity.notFound().build();
        }
        booking.setConfirmation(true);

        bookingRepository.save(booking);
        BookingViewModel bookingViewModel = new BookingViewModel(booking.getUserName(), booking.getSecondaryUserName(), booking.getStartTime(), booking.getEndTime(), booking.getRoomname(), booking.getDate());
        return ResponseEntity.ok(bookingViewModel);
    }

    // Delete a Room
    @DeleteMapping("/bookings/deletebooking/{username}/{date}/{time}/")
    public ResponseEntity<BookingViewModel> deleteBooking(@PathVariable(value = "username") String username, @PathVariable(value = "date") String date, @PathVariable(value = "starttime") float starttime) {
        Booking booking = bookingRepository.findBookingByUserNameAndDateAndStartTimeEquals(username, date, starttime);
        if(booking == null) {
            return ResponseEntity.notFound().build();
        }

        bookingRepository.delete(booking);
        return ResponseEntity.ok().build();
    }

    private List<BookingViewModel> convertToViewModel(List<Booking> bookings) {

        List<BookingViewModel> bmv = new ArrayList<>();

        for(int i = 0; i<bookings.size();i++) {
            bmv.add(new BookingViewModel(bookings.get(i).getUserName(),bookings.get(i).getSecondaryUserName(), bookings.get(i).getStartTime(), bookings.get(i).getEndTime(), bookings.get(i).getRoomname(), bookings.get(i).getDate()));
        }
        return bmv;
    }

    private void deleteIfNecesseary(){
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String dateNow = dateFormat.format(rightnow);
        String timenow = parser.format(rightnow);

        try {
            Date userDate = parser.parse(timenow);
            for(int i = 0; i < starttimes.size(); i++) {
                if (userDate.after(parser.parse(starttimes.get(i) + ":15")) && userDate.before(parser.parse(endtimes.get(i) + ":00"))) {
                    bookingRepository.removeBookingByDateAndStartTimeAndCheckedInFalseOrSecondaryCheckInFalse(dateNow, Float.parseFloat(starttimes.get(i)));
                }
                if(userDate.after(parser.parse(endtimes.get(i) + ":00"))) {
                    bookingRepository.removeBookingByDateAndEndTime(dateNow, Float.parseFloat(endtimes.get(i)));
                }
            }
            bookingRepository.removeAllBookingByDateBefore(dateNow);


        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
