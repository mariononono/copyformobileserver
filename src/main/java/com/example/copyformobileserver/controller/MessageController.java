package com.example.copyformobileserver.controller;

import com.example.copyformobileserver.model.Message;
import com.example.copyformobileserver.repository.MessageRepository;
import com.example.copyformobileserver.viewmodel.MessageViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class MessageController {

    @Autowired
    MessageRepository messageRepository;

    // Get All bookings
    @GetMapping("chat/{receiver}/{sender}")
    public List<MessageViewModel> getAllMessages(@PathVariable(value = "receiver") String receiver, @PathVariable(value = "sender") String sender) {
        return convertToViewModel(messageRepository.findAll());
    }

    @GetMapping("/message/getlatest/{receiver}")
    public List<MessageViewModel> getLatestMessage(@PathVariable(value = "receiver") String receiver,
                                                   @Valid @RequestBody List<String> contacts) {

        String usernamesQuery = "";
        System.out.println("me");

        for(String c : contacts){
            usernamesQuery += "'" + c + "'";
            if(!contacts.get(contacts.size()-1).equals(c))
                usernamesQuery += ",";
        }

        List<Message> messages = messageRepository.findDistinctByReceiverAndSenderIn(receiver, usernamesQuery);

        return convertToViewModel(messages);
    }

    // Create a new Booking
    @PostMapping("/message/send")
    public MessageViewModel createMessage(@Valid @RequestBody Message message) {
        Message m = messageRepository.save(message);
        MessageViewModel messageViewModel = new MessageViewModel(m.getSender(), m.getReceiver(), m.getMessage());
        return messageViewModel;
    }

    private List<MessageViewModel> convertToViewModel(List<Message> messages) {

        List<MessageViewModel> mvm = new ArrayList<>();

        for(int i = 0; i<messages.size();i++) {
            mvm.add(new MessageViewModel(messages.get(i).getSender(), messages.get(i).getReceiver(), messages.get(i).getMessage()));
        }
        return mvm;
    }

}