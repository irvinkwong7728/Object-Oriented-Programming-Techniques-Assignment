package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import models.Staff;

public class StaffDAO {
    public boolean staffEmailExists(String email){
        try (BufferedReader reader = new BufferedReader(new FileReader("staff.txt"))){
            String line;
            while ((line = reader.readLine()) != null){
                String [] column = line.split(",");
                String storedEmail = column[4].trim();
                if (storedEmail.equals(email)){
                    return true;
                } 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Staff loginValidation(String staffID, String password) {
        try {
            File file = new File("staff.txt");
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] data = line.split(",");

                if (data.length == 5) {
                    int storedID = Integer.parseInt(data[0].trim());
                    String storedPassword = data[2].trim();

                    if (staffID.equals(String.valueOf(storedID)) && password.equals(storedPassword)) {
                        sc.close();
                        return new Staff(storedID, data[1], data[2], data[3], data[4]);
                    }
                }
            }
            sc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createStaff(Staff staff) {
        try (FileWriter writer = new FileWriter("staff.txt", true)) {
            writer.write(staff.toString() + "\n");
            }   
            catch (IOException ex) {
            ex.printStackTrace();
        }
    }
     
    public static int generateStaffID() {
        int lastID = 100; 
        try {
            File file = new File("staff.txt");
            if (file.exists()) {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String lastLine = scanner.nextLine();
                    String[] data = lastLine.split(",");
                    lastID = Integer.parseInt(data[0]); 
                }
                scanner.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lastID + 1;
    }
}
