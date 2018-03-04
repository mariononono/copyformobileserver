package com.example.mjukvarukonstruktionbokningserver.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

    @Entity
    @Table(name = "timeinterval")
    @EntityListeners(AuditingEntityListener.class)
    public class TimeInterval implements Serializable {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "Id")
        private int Id;

        @Column(name = "starttime")
        private float startTime;

        @Column(name = "stoptime")
        private float stopTime;

        public int getId() {
            return Id;
        }

        public void setId(int id) {
            Id = id;
        }

        public float getStartTime() {
            return startTime;
        }

        public void setStartTime(float startTime) {
            this.startTime = startTime;
        }

        public float getStopTime() {
            return stopTime;
        }

        public void setStopTime(float stopTime) {
            this.stopTime = stopTime;
        }
    }
