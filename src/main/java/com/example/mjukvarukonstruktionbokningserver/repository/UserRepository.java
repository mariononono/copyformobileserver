package com.example.mjukvarukonstruktionbokningserver.repository;

import com.example.mjukvarukonstruktionbokningserver.model.Booking;
import com.example.mjukvarukonstruktionbokningserver.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUserName(String UserName);

    @Transactional
    @Query("update User u set u.firsthours = ?2 where u.userName = ?1")
    void updateFirstWeekHours(String userName, int firsthours);

    @Transactional
    @Query("update User u set u.secondhours = ?2 where u.userName = ?1")
    void updateSecondWeekHours(String userName, int secondhours);

    @Transactional
    @Query("update User u set u.thirdhours = ?2 where u.userName = ?1")
    void updateThirdWeekHours(String userName, int thirdhours);

}