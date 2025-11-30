package models;

import java.time.LocalDate;
import java.time.LocalTime;

public class Booking {
    private String bookingID;
    private String roomID;
    private int memberID;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    // Constructor
    public Booking(String bookingID, String roomID, int memberID, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.bookingID = bookingID;
        this.roomID = roomID;
        this.memberID = memberID;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters
    public String getBookingID() {
        return bookingID;
    }

    public String getRoomID() {
        return roomID;
    }

    public int getMemberID() {
        return memberID;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    // Setters
    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    // toString
    @Override
    public String toString() {
        return "Booking ID: " + bookingID +
               ", Room ID: " + roomID +
               ", Member ID: " + memberID +
               ", Date: " + date +
               ", Start Time: " + startTime +
               ", End Time: " + endTime;
    }
}
