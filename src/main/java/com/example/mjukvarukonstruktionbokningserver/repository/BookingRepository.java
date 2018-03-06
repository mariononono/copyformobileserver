package com.example.mjukvarukonstruktionbokningserver.repository;

import com.example.mjukvarukonstruktionbokningserver.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByUserNameEquals(String username);

    List<Booking> findBySecondaryUserNameEquals(String secondaryUserName);

    List<Booking> findBookingByDateAndStartTime(String date, float starttime);

    Booking findBookingByUserNameAndDateAndStartTimeEquals(String username, String date, float starttime);

    Booking findBookingBySecondaryUserNameAndDateAndStartTimeEquals(String secondaryUserName, String date, float starttime);

    @Transactional
    void removeBookingByDateAndStartTimeAndCheckedInFalseOrSecondaryCheckInFalse(String date, float starttime);
    @Transactional
    void removeBookingByDateAndEndTime(String date, float endtime);
}