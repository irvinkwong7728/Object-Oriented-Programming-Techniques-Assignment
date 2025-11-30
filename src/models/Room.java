package models;

public class Room {
    private String roomID;
    private String roomType;
    
    public Room(String roomID, String roomType) {
        this.roomID = roomID;
        this.roomType = roomType;
    }

    //getter
    public String getRoomID() {
        return roomID;
    }
    public String getRoomType() {
        return roomType;
    }

    //setter
    public void setRoomID(String roomID){
        this.roomID = roomID;
    }
    public void setRoomType(String roomType){
        this.roomType = roomType;
    }

    public String toString() {
        return roomID + " - " + roomType;
    }
}

