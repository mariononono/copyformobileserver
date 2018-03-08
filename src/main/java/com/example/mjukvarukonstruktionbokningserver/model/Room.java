package com.example.mjukvarukonstruktionbokningserver.model;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "room")
@EntityListeners(AuditingEntityListener.class)
public class Room implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int Id;

    @Column(name = "room_name", unique = true)
    private String roomName;

    @Column(name = "qrcode", unique = true)
    private String qrcode;

    @Column(name = "type")
    private String TYPE;

    @Column(name = "bookablekth")
    private boolean BookableKTH;

    @Column(name = "bookablerkh")
    private boolean BookableRKH;

    @Column(name = "floor")
    private int floor;

    @Column(name = "capacity")
    private int Capacity;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public boolean isBookableKTH() {
        return BookableKTH;
    }

    public void setBookableKTH(boolean bookableKTH) {
        BookableKTH = bookableKTH;
    }

    public boolean isBookableRKH() {
        return BookableRKH;
    }

    public void setBookableRKH(boolean bookableRKH) {
        BookableRKH = bookableRKH;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getCapacity() {
        return Capacity;
    }

    public void setCapacity(int capacity) {
        Capacity = capacity;
    }
}