package data;

import java.io.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;

import models.*;

public class TransactionDAO{
    private static final String FILE_PATH = "transactions.txt";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public void saveTransaction(Transaction transaction){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))){
            String line = transaction.getTransactionID() + "," + transaction.getBorrowID() + "," +
                          transaction.getMemberID() + "," + transaction.getBookID() +"," +
                          sdf.format(transaction.getBorrowDate()) + "," +
                          (transaction.getReturnDate() != null ? sdf.format(transaction.getReturnDate()) : "null") + "," +
                          transaction.isFineApplied();
            writer.write(line);
            writer.newLine();
        }
        catch (IOException e){
            System.out.println("Error saving transaction: " + e.getMessage());
        }
    }

    public List<Transaction> getAllTransactions(){
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))){
            String line;
            while ((line = reader.readLine()) != null){
                String[] parts = line.split(",");

                int transactionID = Integer.parseInt(parts[0]);
                int borrowID = Integer.parseInt(parts[1]);
                int memberID = Integer.parseInt(parts[2]);
                String bookID = parts[3];
                Date borrowDate = sdf.parse(parts[4]);
                Date returnDate = parts[5].equals("null") ? null : sdf.parse(parts[5]);
                boolean isFineApplied = Boolean.parseBoolean(parts[6]);

                transactions.add(new Transaction(transactionID, borrowID, memberID, bookID, borrowDate, returnDate, isFineApplied , 0.0)); 
            }
        }
        catch (IOException | ParseException e){
            System.out.println("Error reading transactions: " + e.getMessage());
        }
        return transactions;
    }

    public void markTransactionAsReturned(int transactionID){
        List<Transaction> transactions = getAllTransactions();
        boolean updated = false;

        for (Transaction t : transactions){
            if (t.getTransactionID() == transactionID){
                t.setReturnDate(new Date());
                updated = true;
                break;
            }
        }

        if (updated){
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))){
                for (Transaction t : transactions){
                    String line = t.getTransactionID() + "," + t.getBorrowID() + "," + t.getMemberID() + "," +
                                  t.getBookID() + "," + sdf.format(t.getBorrowDate()) + "," +
                                  (t.getReturnDate() != null ? sdf.format(t.getReturnDate()) : "null") + "," +
                                  t.isFineApplied();
                    writer.write(line);
                    writer.newLine();
                }
            }
            catch (IOException e){
                System.out.println("Error updating transaction: " + e.getMessage());
            }
        }
    }

    public List<Transaction> getBorrowingRecord(Member currentUser) {
        List<Transaction> transactions = new ArrayList<>();
    
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 7) { 
                    int tID = Integer.parseInt(data[0]);
                    int bID = Integer.parseInt(data[1]);
                    int mID = Integer.parseInt(data[2]);
                    String bookID = data[3];
                    Date borrowDate = sdf.parse(data[4]);
                    Date returnDate = data[5].equals("null") ? null : sdf.parse(data[5]);
                    boolean isFinePaid = Boolean.parseBoolean(data[6]);
    
                    if (mID == currentUser.getMemberId()) { 
                        transactions.add(new Transaction(tID, bID, mID, bookID, borrowDate, returnDate, isFinePaid , 0.0));
                    }
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    
        return transactions;
    }

    public void updateFineStatus(Transaction updatedTransaction) {
        List<Transaction> all = getAllTransactions();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Transaction t : all) {
                if (t.getTransactionID() == updatedTransaction.getTransactionID()) {
                    t.setFineAmount(updatedTransaction.getFineAmount());
                    t.setFineApplied(true);
                }
                writer.write(t.getTransactionID() + "," +
                             t.getBorrowID() + "," +
                             t.getMemberID() + "," +
                             t.getBookID() + "," +
                             sdf.format(t.getBorrowDate()) + "," +
                             (t.getReturnDate() != null ? sdf.format(t.getReturnDate()) : "null") + "," +
                             t.isFineApplied());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    

    public void deleteTransaction(Member currentUser){
        File oldFile = new File(FILE_PATH);
        File newFile = new File("temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(oldFile));
         BufferedWriter writer = new BufferedWriter(new FileWriter(newFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                if (Integer.parseInt(data[2]) == currentUser.getMemberId()) {
                    continue;
                } 
                    writer.write(line);  
                    writer.newLine(); 
                }
            
                } catch (IOException e) {
                    e.printStackTrace();
                }
            
                oldFile.delete();
                newFile.renameTo(oldFile);
            }
}   
