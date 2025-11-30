package data;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import models.Booking;

import javax.swing.JOptionPane;

public class BookingDAO {
    private static final String BOOKINGS_FILE = "bookings.txt";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    private static final LocalTime OPEN_TIME = LocalTime.of(8, 0);
    private static final LocalTime CLOSE_TIME = LocalTime.of(17, 0);

    public boolean createBooking(Booking bookingWithoutID) {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        if (!bookingWithoutID.getDate().isEqual(today) && !bookingWithoutID.getDate().isEqual(tomorrow)) {
            JOptionPane.showMessageDialog(null, "Bookings can only be made for today or tomorrow!", "Invalid Date", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!isBookingTimeValid(bookingWithoutID.getStartTime(), bookingWithoutID.getEndTime())) {
            JOptionPane.showMessageDialog(null, "Booking must be between 1-2 hours, and between 8 AM and 5 PM!", "Invalid Time", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (isRoomAvailable(bookingWithoutID.getRoomID(), bookingWithoutID.getDate(), bookingWithoutID.getStartTime(), bookingWithoutID.getEndTime())) {
            String generatedBookingID = generateBookingID();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(BOOKINGS_FILE, true))) {
                bw.write(generatedBookingID + "," + bookingWithoutID.getRoomID() + "," + bookingWithoutID.getMemberID() + "," +
                        bookingWithoutID.getDate().format(DATE_FORMAT) + "," + bookingWithoutID.getStartTime().format(TIME_FORMAT) + "," +
                        bookingWithoutID.getEndTime().format(TIME_FORMAT));
                bw.newLine();
                JOptionPane.showMessageDialog(null, "Booking successful! Your Booking ID: " + generatedBookingID, "Success", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error saving booking: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(null, "Room is not available at the selected time!", "Room Unavailable", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

    private String generateBookingID() {
        List<Booking> bookings = getAllBookings();
        int maxNumber = 0;
        for (Booking booking : bookings) {
            String id = booking.getBookingID();
            if (id.startsWith("B")) {
                try {
                    int number = Integer.parseInt(id.substring(1));
                    if (number > maxNumber) {
                        maxNumber = number;
                    }
                } catch (NumberFormatException ignored) { }
            }
        }
        return String.format("B%03d", maxNumber + 1);
    }

    private boolean isBookingTimeValid(LocalTime start, LocalTime end) {
        if (start.isBefore(OPEN_TIME) || end.isAfter(CLOSE_TIME)) {
            return false;
        }
        Duration duration = Duration.between(start, end);
        long hours = duration.toHours();
        return hours >= 1 && hours <= 2;
    }

    private boolean isRoomAvailable(String roomID, LocalDate date, LocalTime start, LocalTime end) {
        List<Booking> bookings = getAllBookings();
        for (Booking booking : bookings) {
            if (booking.getRoomID().equalsIgnoreCase(roomID) && booking.getDate().isEqual(date)) {
                if (start.isBefore(booking.getEndTime()) && end.isAfter(booking.getStartTime())) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(BOOKINGS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    String bookingID = parts[0];
                    String roomID = parts[1];
                    int memberID = Integer.parseInt(parts[2]);
                    LocalDate date = LocalDate.parse(parts[3], DATE_FORMAT);
                    LocalTime startTime = LocalTime.parse(parts[4], TIME_FORMAT);
                    LocalTime endTime = LocalTime.parse(parts[5], TIME_FORMAT);

                    bookings.add(new Booking(bookingID, roomID, memberID, date, startTime, endTime));
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading bookings: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return bookings;
    }

    public boolean cancelBooking(String bookingID) {
        File oldFile = new File(BOOKINGS_FILE);
        File newFile = new File("temp.txt");
        boolean bookingFound = false;
    
        try (BufferedReader reader = new BufferedReader(new FileReader(oldFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(newFile))) {
    
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length > 0 && data[0].equalsIgnoreCase(bookingID)) {
                    bookingFound = true;
                    continue; 
                }
                writer.write(line);
                writer.newLine();
            }
    
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    
        if (bookingFound) {
            if (oldFile.delete()) {
                if (newFile.renameTo(oldFile)) {
                    return true;
                }  
            }  
        } 
        return false;
    }
    

    public Booking getBookingById(String bookingID) {
        try (BufferedReader reader = new BufferedReader(new FileReader(BOOKINGS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String storedID = data[0];
                if (storedID.equals(bookingID)) {
                    return new Booking(data[0], data[1], Integer.parseInt(data[2]), LocalDate.parse(data[3], DATE_FORMAT), LocalTime.parse(data[4], TIME_FORMAT), LocalTime.parse(data[5], TIME_FORMAT));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;  
    }
    
    public boolean updateBookingTime(String bookingID, LocalTime newStartTime, LocalTime newEndTime) {
        File oriFile = new File(BOOKINGS_FILE);
        File updatedFile = new File("temp.txt");
        boolean updated = false;
    
        try (
            BufferedReader reader = new BufferedReader(new FileReader(oriFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(updatedFile))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(bookingID)) {
                    parts[4] = newStartTime.toString();
                    parts[5] = newEndTime.toString();
                    String updatedLine = String.join(",", parts);
                    writer.write(updatedLine);
                    updated = true;
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        oriFile.delete();
        updatedFile.renameTo(oriFile);
    
        return updated;
    }
    
    
}

