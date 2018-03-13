package com.example.copyformobileserver.repository;


import com.example.copyformobileserver.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUserName(String username);

    User findByUserNameAndPassword(String username, String password);

    List<User> findAllByUserName(String username);

}