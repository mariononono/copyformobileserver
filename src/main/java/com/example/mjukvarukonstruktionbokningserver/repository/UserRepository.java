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

}