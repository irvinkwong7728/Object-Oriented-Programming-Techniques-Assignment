package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import models.Member;
import models.Student; 
import models.Faculty;

public class MemberDAO {
    private static final String FILE_PATH = "member.txt";
   
    //CRUD

    public void createMember(Member member) {
        try (FileWriter writer = new FileWriter(FILE_PATH , true)) {
            String changeCaps = member.getName().toUpperCase();
            member.setName(changeCaps);
            writer.write(member.toString() + "\n");
            }   
            catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public boolean memberEmailExists(String email) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[4].equals(email)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
     
    public void updateMember(Member currentUser){
        File oriFile = new File(FILE_PATH);
        File updatedFile = new File("temp.txt");

        try (
            BufferedReader reader = new BufferedReader(new FileReader(oriFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(updatedFile , true))
        ) {
            String line;
            while((line = reader.readLine()) != null){
                String[] parts = line.split(",");
                if (parts[0].equals(String.valueOf(currentUser.getMemberId()))){
                    String updatedLine = currentUser.toString();
                    String changeCaps = currentUser.getName().toUpperCase();
                    currentUser.setName(changeCaps);
                    writer.write(updatedLine);
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
    }

    public Member loginValidation(String memberID, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))){

            String line;
                while((line = reader.readLine()) != null){
                    String[] data = line.split(",");
                    int storedID = Integer.parseInt(data[0].trim());
                    String storedPassword = data[2].trim();
                    String role = data[5].trim();
                    if (memberID.equals(String.valueOf(storedID)) && password.equals(storedPassword)) {
                        if(role.equals("Student")){
                            return new Student(storedID, data[1], data[2], data[3], data[4] , data[5] , Integer.parseInt(data[6]));
                        } else if(role.equals("Faculty")){
                            return new Faculty(storedID, data[1], data[2], data[3], data[4] , data[5] , Integer.parseInt(data[6]));
                        }
                    }
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteMember(Member currentUser){
        File mbrFile = new File(FILE_PATH);
        File newFile = new File("temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(mbrFile));
         BufferedWriter writer = new BufferedWriter(new FileWriter(newFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                if (Integer.parseInt(data[0]) == currentUser.getMemberId()) {
                    continue;
                } 
                    writer.write(line);  
                    writer.newLine(); 
                }
            
                } catch (IOException e) {
                    e.printStackTrace();
                }
            
                mbrFile.delete();
                newFile.renameTo(mbrFile);
            }

    public static int generateMemberID() {
        int lastID = 1000;
        try {
            File file = new File("member.txt");
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

    public void updateBorrowedCount(Member currentUser) {
        File oriFile = new File(FILE_PATH);
        File updatedFile = new File("temp.txt");

        try (
            BufferedReader reader = new BufferedReader(new FileReader(oriFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(updatedFile , true))
        ) {
            String line;
            while((line = reader.readLine()) != null){
                String[] parts = line.split(",");
                if (parts[0].equals(String.valueOf(currentUser.getMemberId()))){
                    int lastBorrowedCount = Integer.parseInt(parts[6].trim()); 
                    currentUser.setBorrowedCount(lastBorrowedCount + 1);
                    String updatedLine = currentUser.toString();
                    writer.write(updatedLine);
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
      
    }

    public void updateReturnedCount(Member currentUser) {
        File oriFile = new File(FILE_PATH);
        File updatedFile = new File("temp.txt");

        try (
            BufferedReader reader = new BufferedReader(new FileReader(oriFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(updatedFile , true))
        ) {
            String line;
            while((line = reader.readLine()) != null){
                String[] parts = line.split(",");
                if (parts[0].equals(String.valueOf(currentUser.getMemberId()))){
                    int lastBorrowedCount = Integer.parseInt(parts[6].trim()); 
                    currentUser.setBorrowedCount(lastBorrowedCount - 1);
                    String updatedLine = currentUser.toString();
                    writer.write(updatedLine);
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
      
    }

    public Member getMemberByID(int memberID) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                int storedID = Integer.parseInt(data[0].trim()); 
                String role = data[5].trim(); 
                if (storedID == memberID) {
                    if(role.equals("Student")){
                        return new Student(storedID, data[1], data[2], data[3], data[4] , data[5] , Integer.parseInt(data[6]));
                    } else if(role.equals("Faculty")){
                        return new Faculty(storedID, data[1], data[2], data[3], data[4] , data[5] , Integer.parseInt(data[6]));
                    }
                    
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;  
    }
    
    
}












