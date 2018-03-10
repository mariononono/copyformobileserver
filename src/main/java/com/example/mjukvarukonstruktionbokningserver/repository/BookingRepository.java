package com.example.mjukvarukonstruktionbokningserver.repository;

import com.example.mjukvarukonstruktionbokningserver.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByUserNameEquals(String username);

    List<Booking> findBySecondaryUserNameEquals(String secondaryUserName);

    List<Booking> findBookingByDateAndStartTime(Date date, float starttime);

    List<Booking> findBookingByDate(Date date);

    Booking findBookingByUserNameAndDateAndStartTimeEquals(String username, Date date, float starttime);

    Booking findBookingBySecondaryUserNameAndDateAndStartTimeEquals(String secondaryUserName, Date date, float starttime);

    @Transactional
    void removeBookingByDateAndStartTimeAndCheckedEquals(Date date, float starttime, boolean checked);
    @Transactional
    void removeBookingByDateAndStartTimeAndSecondaryCheckedEquals(Date date, float starttime, boolean secondaryChecked);
    @Transactional
    void removeBookingByDateAndStartTime(Date date, float starttime);
    @Transactional
    void removeBookingByDateAndEndTime(Date date, float endtime);
    @Transactional
    void removeByDateBefore(Date date);

}