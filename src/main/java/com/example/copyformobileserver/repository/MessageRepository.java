package com.example.copyformobileserver.repository;

import com.example.copyformobileserver.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    List<Message> findDistinctByReceiverAndSenderIn(String receiver, String userQuery);

}