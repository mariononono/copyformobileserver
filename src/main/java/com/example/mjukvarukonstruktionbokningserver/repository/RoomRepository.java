package com.example.mjukvarukonstruktionbokningserver.repository;

import com.example.mjukvarukonstruktionbokningserver.model.Booking;
import com.example.mjukvarukonstruktionbokningserver.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;


@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

    List<Room> findByFloor(Integer floor);

    Room findByRoomName(String RoomName);

    @Transactional
    @Query("select distinct u.floor from Room u")
    List<Integer> findByFloor();

}