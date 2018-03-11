package com.example.mjukvarukonstruktionbokningserver.controller;

import com.example.mjukvarukonstruktionbokningserver.model.*;
import com.example.mjukvarukonstruktionbokningserver.repository.*;
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
    @Autowired
    AdminSettingsRepository adminSettingsRepository;


    // Get All bookings
    @GetMapping("/teacher/{date}/allbookings")
    public List<BookingViewModel> getAllBookings(@PathVariable(value = "date") String datestring) {
        deleteIfNecesseary();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(datestring);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<BookingViewModel> bookingViewModels = convertToViewModel(bookingRepository.findBookingByDate(date));

        return bookingViewModels;
    }

    // Create a new Booking
    @PostMapping("/bookings/create")
    public boolean createBooking(@Valid @RequestBody Booking booking) {
        User user = userRepository.findByUserName(booking.getUserName());
        User seconduser = userRepository.findByUserName(booking.getSecondaryUserName());

        if (user != null) {

            if (seconduser != null  || user.getAffiliation().equals("teacher") || user.isAdmin() && !seconduser.getAffiliation().equals("teacher")) {

                if (user.isAdmin() || user.getAffiliation().equals("teacher")) {
                    booking.setChecked(true);
                    booking.setSecondaryChecked(true);
                    Booking b = bookingRepository.save(booking);
                    return true;
                }
                Booking isexisting = bookingRepository.findBookingByUserNameAndDateAndStartTimeEquals(booking.getUserName(), booking.getDate(), booking.getStartTime());
                if (isexisting == null) {
                    isexisting = bookingRepository.findBookingBySecondaryUserNameAndDateAndStartTimeEquals(booking.getUserName(), booking.getDate(), booking.getStartTime());
                }
                if (isexisting == null) {
                    isexisting = bookingRepository.findBookingByUserNameAndDateAndStartTimeEquals(booking.getSecondaryUserName(), booking.getDate(), booking.getStartTime());
                }
                if (isexisting == null) {
                    isexisting = bookingRepository.findBookingBySecondaryUserNameAndDateAndStartTimeEquals(booking.getSecondaryUserName(), booking.getDate(), booking.getStartTime());
                }
                if (isexisting != null)
                    return false;
            } else {
                return false;
            }
        } else {
            return false;
        }

        //ordnar upp timmarna rätt
        Date date = new Date();
        Date bookingDate = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String d = df.format(date);
        String bookingD = df.format(booking.getDate());
        try {
            date = df.parse(d);
            bookingDate = df.parse(bookingD);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        Calendar bookingCal = Calendar.getInstance();
        bookingCal.setTime(bookingDate);

        int bookingweek = bookingCal.get(Calendar.WEEK_OF_YEAR);
        int current = cal.get(Calendar.WEEK_OF_YEAR);
        cal.add(cal.DATE, +7);
        int second = cal.get(Calendar.WEEK_OF_YEAR);
        cal.add(cal.DATE, +14);
        int third = cal.get(Calendar.WEEK_OF_YEAR);

        if (!rearrangeIfNecessary(user, seconduser, current, second, third))
            return false;

        int hoursleft = 0;
        int hoursToReduce = (int) (booking.getEndTime()-booking.getStartTime());
        if (bookingweek == current) {
            hoursleft = user.getFirsthours();
            if ((hoursleft - hoursToReduce) >= 0) {
                user.setFirsthours(hoursleft - hoursToReduce);
            } else {
                return false;
            }
            hoursleft = seconduser.getFirsthours();
            if ((hoursleft - hoursToReduce) >= 0) {
                seconduser.setFirsthours(hoursleft - hoursToReduce);
            } else {
                return false;
            }
        } else if (bookingweek == second) {
            hoursleft = user.getSecondhours();
            if ((hoursleft - hoursToReduce) >= 0) {
                user.setSecondhours(hoursleft - hoursToReduce);
            } else {
                return false;
            }
            hoursleft = seconduser.getSecondhours();
            if ((hoursleft - hoursToReduce) >= 0) {
                seconduser.setSecondhours(hoursleft - hoursToReduce);
            } else {
                return false;
            }
        } else if (bookingweek == third) {
            hoursleft = user.getThirdhours();
            if ((hoursleft - hoursToReduce) >= 0) {
                user.setThirdhours(hoursleft - hoursToReduce);
            } else {
                return false;
            }
            hoursleft = seconduser.getThirdhours();
            if ((hoursleft - hoursToReduce) >= 0) {
                seconduser.setThirdhours(hoursleft - hoursToReduce);
            } else {
                return false;
            }
        } else {
            return false;
        }

        Booking b = bookingRepository.save(booking);

        return true;
    }

    // Get a Single Room
    @GetMapping("/bookings/{username}/{date}/{starttime}/")
    public ResponseEntity<Booking> getBookingById(@PathVariable(value = "username") String username, @PathVariable(value = "date") String datestring, @PathVariable(value = "starttime") float starttime) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(datestring);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Booking booking = bookingRepository.findBookingByUserNameAndDateAndStartTimeEquals(username, date, starttime);
        if (booking == null) {
            booking = bookingRepository.findBookingBySecondaryUserNameAndDateAndStartTimeEquals(username, date, starttime);
        }
        if (booking == null) {
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
    public ResponseEntity<BookingViewModel> updateBooking(@PathVariable(value = "username") String username, @PathVariable(value = "date") String datestring, @PathVariable(value = "starttime") float starttime, @PathVariable(value = "qr") String qr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(datestring);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Booking booking = bookingRepository.findBookingByUserNameAndDateAndStartTimeEquals(username, date, starttime);
        if (booking == null) {
            booking = bookingRepository.findBookingBySecondaryUserNameAndDateAndStartTimeEquals(username, date, starttime);
        }
        if (booking == null) {
            return ResponseEntity.notFound().build();
        }

        Room room = roomRepository.findByRoomName(booking.getRoomname());
        if (room == null) {
            return ResponseEntity.notFound().build();
        }

        if (username.equals(booking.getUserName()) && !booking.isChecked()) {
            if (qr.equals(room.getQrcode()))
                booking.setChecked(true);
        } else if (username.equals(booking.getSecondaryUserName()) && !booking.isSecondaryChecked()) {
            if (qr.equals(room.getQrcode()))
                booking.setSecondaryChecked(true);
        }

        Booking updatedbooking = bookingRepository.save(booking);

        String dateToSave = dateFormat.format(updatedbooking.getDate());

        BookingViewModel bookingViewModel = new BookingViewModel(updatedbooking.getUserName(), updatedbooking.getSecondaryUserName(), updatedbooking.getStartTime(), updatedbooking.getEndTime(), updatedbooking.getRoomname(), updatedbooking.isChecked(), updatedbooking.isSecondaryChecked(), dateToSave);

        return ResponseEntity.ok(bookingViewModel);
    }

    // Update a Booking
    @PutMapping("/bookings/updatebooking/{username}/{date}/{starttime}/")
    public boolean updateBooking(@PathVariable(value = "username") String username, @PathVariable(value = "date") String datestring, @PathVariable(value = "starttime") float starttime,
                                 @Valid @RequestBody Booking bookingDetails) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(datestring);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Booking booking = bookingRepository.findBookingByUserNameAndDateAndStartTimeEquals(username, date, starttime);
        if (booking == null) {
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
    @DeleteMapping("/bookings/deletebooking/{username}/")
    public boolean deleteBooking(@PathVariable(value = "username") String username,
            @Valid @RequestBody Booking bookingDetails) {
        Booking booking = bookingRepository.findBookingByUserNameAndDateAndStartTimeEquals(bookingDetails.getUserName(), bookingDetails.getDate(), bookingDetails.getStartTime());

        if (booking == null) {
            booking = bookingRepository.findBookingByUserNameAndDateAndStartTimeEquals(bookingDetails.getSecondaryUserName(), bookingDetails.getDate(), bookingDetails.getStartTime());
        }

        if (booking == null) {
            return false;
        }

        float starttime = bookingDetails.getStartTime();
        float before = starttime - 2;

        String tmpstart = Float.toString(starttime);
        String[] tmparraystart = tmpstart.split("\\.");
        String tmpbefore = Float.toString(before);
        String[] tmparraybefore = tmpbefore.split("\\.");

        Date rightnow = Calendar.getInstance().getTime();
        Date rnow = new Date();
        Date bookingDate = null;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
        String timenow = parser.format(rightnow);
        String dateNow = dateFormat.format(rnow);
        String bookingD = dateFormat.format(booking.getDate());

        try {
            bookingDate = dateFormat.parse(bookingD);
            Date now = dateFormat.parse(dateNow);
            Date userTime = parser.parse(timenow);
            Date startTime = parser.parse(tmparraystart[0] + ":00");
            Date beforTime = parser.parse(tmparraybefore[0] + ":00");

            if (userTime.after(beforTime) && userTime.before(startTime) && now.equals(booking.getDate())) {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar bookingCal = Calendar.getInstance();
        bookingCal.setTime(bookingDate);

        Calendar cal = Calendar.getInstance();

        int bookingweek = bookingCal.get(Calendar.WEEK_OF_YEAR);
        int current = cal.get(Calendar.WEEK_OF_YEAR);
        cal.add(cal.DATE, +7);
        int second = cal.get(Calendar.WEEK_OF_YEAR);
        cal.add(cal.DATE, +14);
        int third = cal.get(Calendar.WEEK_OF_YEAR);

        User user = userRepository.findByUserName(booking.getUserName());
        User seconduser = userRepository.findByUserName(booking.getSecondaryUserName());
        if(!rearrangeIfNecessary(user, seconduser, current, second, third))
            return false;

        int firsth = 0;
        int secondh = 0;
        int thirdh = 0;

        if (current == bookingweek) {
            firsth = user.getFirsthours();
            user.setFirsthours(firsth + 2);
            firsth = seconduser.getFirsthours();
            seconduser.setFirsthours(firsth + 2);
        } else if (second == bookingweek) {
            secondh = user.getSecondhours();
            user.setFirsthours(secondh);
            secondh = seconduser.getSecondhours();
            seconduser.setSecondhours(secondh);
        } else if (third == bookingweek) {
            thirdh = user.getThirdhours();
            user.setThirdhours(thirdh);
            thirdh = seconduser.getThirdhours();
            seconduser.setThirdhours(thirdh);
        } else {
            return false;
        }

        if(!username.equals(booking.getUserName()) || !username.equals(booking.getSecondaryUserName())) {
            //skicka mail
        }

        bookingRepository.delete(booking);
        return true;
    }

    private List<BookingViewModel> convertToViewModel(List<Booking> bookings) {

        List<BookingViewModel> bmv = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = "";

        for (int i = 0; i < bookings.size(); i++) {
            date = dateFormat.format(bookings.get(i).getDate());
            bmv.add(new BookingViewModel(bookings.get(i).getUserName(), bookings.get(i).getSecondaryUserName(), bookings.get(i).getStartTime(), bookings.get(i).getEndTime(), bookings.get(i).getRoomname(), bookings.get(i).isChecked(), bookings.get(i).isSecondaryChecked(), date));
        }
        return bmv;
    }

    public void deleteIfNecesseary() {
        List<TimeInterval> timeIntervals = timeIntervalRepository.findAll();
        List<String> starttimes = new ArrayList<>();
        List<String> endtimes = new ArrayList<>();

        for (int i = 0; i < timeIntervals.size(); i++) {
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
            for (int i = 0; i < starttimes.size(); i++) {
                if (userDate.after(parser.parse(starttimes.get(i) + ":15")) && userDate.before(parser.parse(endtimes.get(i) + ":00"))) {
                    bookingRepository.removeBookingByDateAndStartTimeAndCheckedEquals(now, Float.parseFloat(starttimes.get(i)), false);
                    bookingRepository.removeBookingByDateAndStartTimeAndSecondaryCheckedEquals(now, Float.parseFloat(starttimes.get(i)), false);
                }
                if (userDate.after(parser.parse(endtimes.get(i) + ":00"))) {
                    bookingRepository.removeBookingByDateAndEndTime(now, Float.parseFloat(endtimes.get(i)));
                }
            }

            bookingRepository.removeByDateBefore(now);


        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public boolean rearrangeIfNecessary(User user, User seconduser, int current, int second, int third) {
        List<AdminSettings> adminSettings = adminSettingsRepository.findAll();
        if (adminSettings.isEmpty())
            return false;

        if (adminSettings.size() > 1) {
            return false;
        }

        int maxhours = adminSettings.get(0).getMaxhours();

        if (current == user.getCurrentweek()) {
        } else if (second == user.getCurrentweek()) {
            user.setFirsthours(user.getSecondhours());
            user.setSecondhours(user.getThirdhours());
            user.setThirdhours(maxhours);
            seconduser.setFirsthours(seconduser.getSecondhours());
            seconduser.setSecondhours(seconduser.getThirdhours());
            seconduser.setThirdhours(maxhours);
            user.setCurrentweek(current);
        } else if (third == user.getCurrentweek()) {
            user.setFirsthours(user.getThirdhours());
            user.setSecondhours(maxhours);
            user.setThirdhours(maxhours);
            seconduser.setFirsthours(seconduser.getThirdhours());
            seconduser.setSecondhours(maxhours);
            seconduser.setThirdhours(maxhours);
            user.setCurrentweek(current);
        } else {
            user.setFirsthours(maxhours);
            user.setSecondhours(maxhours);
            user.setThirdhours(maxhours);
            seconduser.setFirsthours(maxhours);
            seconduser.setSecondhours(maxhours);
            seconduser.setThirdhours(maxhours);
            user.setCurrentweek(current);
        }
        return true;
    }

}
