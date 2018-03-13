package com.example.copyformobileserver.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

    @Entity
    @Table(name = "message")
    @EntityListeners(AuditingEntityListener.class)
    public class Message implements Serializable {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "Id")
        private int Id;

        @Column(name = "sender", nullable = false)
        private String sender;

        @Column(name = "receiver", nullable = false)
        private String receiver;

        @Column(name = "message")
        private String message;

        public int getId() {
            return Id;
        }

        public void setId(int id) {
            Id = id;
        }

        public String getSender() {
            return sender;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }

        public String getReceiver() {
            return receiver;
        }

        public void setReceiver(String receiver) {
            this.receiver = receiver;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
