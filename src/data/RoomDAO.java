package data;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

import models.Booking;
import models.Room;

public class RoomDAO {
    private static final String FILE_PATH = "rooms.txt";

    public String generateRoomID() {
        List<Room> rooms = listRooms();
        int maxNumber = 0;
        for (Room room : rooms) {
            String id = room.getRoomID();
            if (id.startsWith("R")) {
                try {
                    int number = Integer.parseInt(id.substring(1));
                    if (number > maxNumber) {
                        maxNumber = number;
                    }
                } catch (NumberFormatException ignored) { }
            }
        }
        return String.format("R%03d", maxNumber + 1);
    }

    public void addRoom(String roomID, String roomType) {
        List<Room> rooms = listRooms();
        for (Room room : rooms) {
            if (room.getRoomID().equals(roomID)) {
                JOptionPane.showMessageDialog(null, "Room with ID " + roomID + " already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        Room room = new Room(roomID, roomType);
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(room.getRoomID() + "," + room.getRoomType());
            writer.newLine();
            JOptionPane.showMessageDialog(null, "Room Added Successfully!", "Info", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error Saving Room", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<Room> listRooms() {
        List<Room> rooms = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] roomDetails = line.split(",");
                String roomID = roomDetails[0];
                String roomType = roomDetails[1];

                Room room = new Room(roomID, roomType);
                rooms.add(room);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error Reading Rooms", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return rooms;
    }

    public void deleteRoom(String roomID) {
        List<Room> rooms = listRooms();
        boolean found = false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Room room : rooms) {
                if (!room.getRoomID().equals(roomID)) {
                    writer.write(room.getRoomID() + "," + room.getRoomType());
                    writer.newLine();
                } else {
                    found = true;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error Deleting Room", "Error", JOptionPane.ERROR_MESSAGE);
        }

        if (found) {
            JOptionPane.showMessageDialog(null, "Room Deleted Successfully!", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Room Not Found!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    public Room getRoomByID(String roomID) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] roomDetails = line.split(",");
                if (roomDetails[0].equals(roomID)) {
                    String roomType = roomDetails[1];
                    return new Room(roomID, roomType);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error Reading Room Info", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    private boolean isRoomAvailable(String roomID, LocalDate date, LocalTime start, LocalTime end) {
        List<Booking> bookings = new BookingDAO().getAllBookings();
        for (Booking booking : bookings) {
            if (booking.getRoomID().equalsIgnoreCase(roomID) && booking.getDate().isEqual(date)) {
                if (start.isBefore(booking.getEndTime()) && end.isAfter(booking.getStartTime())) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<String> showAvailableRooms(LocalDate date, LocalTime start, LocalTime end) {
        List<String> availableRooms = new ArrayList<>();
        List<Room> allRooms = new RoomDAO().listRooms();
        for (Room room : allRooms) {
            if (isRoomAvailable(room.getRoomID(), date, start, end)) {
                availableRooms.add(room.getRoomID() + " - " + room.getRoomType());
            }
        }
        return availableRooms;
    }
}


