package com.example.mjukvarukonstruktionbokningserver.controller;

import com.example.mjukvarukonstruktionbokningserver.model.Booking;
import com.example.mjukvarukonstruktionbokningserver.model.Room;
import com.example.mjukvarukonstruktionbokningserver.model.TimeInterval;
import com.example.mjukvarukonstruktionbokningserver.model.User;
import com.example.mjukvarukonstruktionbokningserver.repository.BookingRepository;
import com.example.mjukvarukonstruktionbokningserver.repository.RoomRepository;
import com.example.mjukvarukonstruktionbokningserver.repository.TimeIntervalRepository;
import com.example.mjukvarukonstruktionbokningserver.repository.UserRepository;
import com.example.mjukvarukonstruktionbokningserver.viewmodel.BookingViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public boolean createBooking(@Valid @RequestBody Booking booking) {
        User user = userRepository.findByUserName(booking.getUserName());
        if(user != null) {
            if (user.isAdmin() || user.getAffiliation().equals("teacher")) {
                booking.setChecked(true);
                booking.setSecondaryChecked(true);
            }
            Booking isexisting = bookingRepository.findBookingByUserNameAndDateAndStartTimeEquals(booking.getUserName(), booking.getDate(), booking.getStartTime());

            if(isexisting != null && !user.getAffiliation().equals("teacher"))
                return false;

            isexisting = bookingRepository.findBookingBySecondaryUserNameAndDateAndStartTimeEquals(booking.getUserName(), booking.getDate(), booking.getStartTime());

            if(isexisting != null && !user.getAffiliation().equals("teacher"))
                return false;
        }

        Booking b = bookingRepository.save(booking);
        return true;
    }

    // Get a Single Room
    @GetMapping("/bookings/{username}/{date}/{starttime}/")
    public ResponseEntity<Booking> getBookingById(@PathVariable(value = "username") String username, @PathVariable(value = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date, @PathVariable(value = "starttime") float starttime) {
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

    // testa med post... eller sök på put
    @PutMapping("/bookings/checkin/{username}/{date}/{starttime}/{qr}/")
    public ResponseEntity<BookingViewModel> updateBooking(@PathVariable(value = "username") String username, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date, @PathVariable(value = "starttime") float starttime, @PathVariable(value = "qr") String qr) {
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

        if(username.equals(booking.getUserName()) && !booking.isChecked()) {
            if(qr.equals(room.getQrcode()))
                booking.setChecked(true);
        } else if(username.equals(booking.getSecondaryUserName()) && !booking.isSecondaryChecked()) {
            if(qr.equals(room.getQrcode()))
                booking.setSecondaryChecked(true);
        }

        Booking updatedbooking = bookingRepository.save(booking);

        BookingViewModel bookingViewModel = new BookingViewModel(updatedbooking.getUserName(), updatedbooking.getSecondaryUserName(), updatedbooking.getStartTime(), updatedbooking.getEndTime(), updatedbooking.getRoomname(), updatedbooking.isChecked(), updatedbooking.isSecondaryChecked(), updatedbooking.getDate());

        return ResponseEntity.ok(bookingViewModel);
    }

    // Update a Booking
    @PutMapping("/bookings/updatebooking/{username}/{date}/{starttime}/")
    public boolean updateBooking(@PathVariable(value = "username") String username, @PathVariable(value = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date, @PathVariable(value = "starttime") float starttime,
                                           @Valid @RequestBody Booking bookingDetails) {
        Booking booking = bookingRepository.findBookingByUserNameAndDateAndStartTimeEquals(username, date, starttime);
        if(booking == null) {
            return false;
        }
        booking.setUserName(bookingDetails.getUserName());
        booking.setStartTime(bookingDetails.getStartTime());
        booking.setRoomname(bookingDetails.getRoomname());
        booking.setDate(bookingDetails.getDate());
        booking.setEndTime(bookingDetails.getEndTime());
        booking.setSecondaryUserName(bookingDetails.getSecondaryUserName());

        bookingRepository.save(booking);
        return true;
    }

    // Delete booking
    @DeleteMapping("/bookings/deletebooking/")
    public boolean deleteBooking(@Valid @RequestBody Booking bookingDetails) {
        Booking booking = bookingRepository.findBookingByUserNameAndDateAndStartTimeEquals(bookingDetails.getUserName(), bookingDetails.getDate(), bookingDetails.getStartTime());

        if(booking == null) {
            return false;
        }

        float starttime = bookingDetails.getStartTime();
        float before = starttime - 2;

        String tmpstart = Float.toString(starttime);
        String[] tmparraystart = tmpstart.split("\\.");
        String tmpbefore = Float.toString(before);
        String[] tmparraybefore = tmpbefore.split("\\.");

        Date rightnow = Calendar.getInstance().getTime();

        SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
        String timenow = parser.format(rightnow);

        try {
            Date userTime = parser.parse(timenow);
            Date startTime = parser.parse(tmparraystart[0] + ":00");
            Date beforTime = parser.parse(tmparraybefore[0] + ":00");

            if (userTime.after(beforTime) && userTime.before(startTime)) {
                    return false;
                }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        bookingRepository.delete(booking);
        return true;
    }

    private List<BookingViewModel> convertToViewModel(List<Booking> bookings) {

        List<BookingViewModel> bmv = new ArrayList<>();

        for(int i = 0; i<bookings.size();i++) {
            bmv.add(new BookingViewModel(bookings.get(i).getUserName(),bookings.get(i).getSecondaryUserName(), bookings.get(i).getStartTime(), bookings.get(i).getEndTime(), bookings.get(i).getRoomname(), bookings.get(i).isChecked(), bookings.get(i).isSecondaryChecked(), bookings.get(i).getDate()));
        }
        return bmv;
    }

    public void deleteIfNecesseary(){
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
        }

    }

}
