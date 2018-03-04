package com.example.mjukvarukonstruktionbokningserver.viewmodel;

public class RoomViewModel {

    private String roomName;

    private String TYPE;

    private String QRcode;

    private int floor;

    private int Capacity;

    private boolean BookableKTH;

    private boolean BookableRKH;

    public RoomViewModel(String roomName, String TYPE, String QRcode, int floor, int capacity, boolean bookableKTH, boolean bookableRKH) {
        this.roomName = roomName;
        this.TYPE = TYPE;
        this.QRcode = QRcode;
        this.floor = floor;
        Capacity = capacity;
        BookableKTH = bookableKTH;
        BookableRKH = bookableRKH;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
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

    public String getQRcode() {
        return QRcode;
    }

    public void setQRcode(String QRcode) {
        this.QRcode = QRcode;
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
}
