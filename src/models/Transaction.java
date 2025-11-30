package models;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import data.BookDAO;

public class Transaction{
    private int transactionID;
    private int borrowID;
    private int memberID;
    private String bookID;
    private Date borrowDate;
    private Date returnDate;
    private boolean isFineApplied;
    private double fineAmount;  

    public Transaction(int transactionID, int borrowID, int memberID, String bookID, Date borrowDate, Date returnDate, boolean isFineApplied, double fineAmount) {
        this.transactionID = transactionID;
        this.borrowID = borrowID;
        this.memberID = memberID;
        this.bookID = bookID;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.isFineApplied = isFineApplied;
        this.fineAmount = fineAmount;  
    } 

    public int getTransactionID() {
        return transactionID;
    }

    public int getBorrowID(){
        return borrowID;
    }

    public int getMemberID(){
        return memberID;
    }

    public String getBookID(){
        return bookID;
    }

    public Date getBorrowDate(){
        return borrowDate;
    }

    public Date getReturnDate(){
        return returnDate;
    }

    public boolean isFineApplied(){
        return isFineApplied;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public String getBookTitle() {
        BookDAO bookDAO = new BookDAO();
        return bookDAO.getBookTitleByID(this.bookID);
    }

    public void setFineApplied(boolean isFineApplied){
        this.isFineApplied = isFineApplied;
    }

    public void setReturnDate(Date returnDate){
        this.returnDate = returnDate;
    }

    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }

    public void applyFine(Member member) {
        int overdueDays = calculateOverduedays(member);
        if (overdueDays > 0) {
            this.fineAmount = overdueDays * member.getFineRate();  
            this.isFineApplied = true;  
        } else {
            this.fineAmount = 0.0;  
            this.isFineApplied = false;  
        }
    }

    public int calculateOverduedays(Member member) {
        if (returnDate == null) {
            return 0; 
        }
        
        LocalDate borrowLocal = Instant.ofEpochMilli(borrowDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate returnLocal = Instant.ofEpochMilli(returnDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        
        long daysBetween = ChronoUnit.DAYS.between(borrowLocal, returnLocal);
        int overdueDays = (int) daysBetween - member.getMaxBorrowDays();
        
        return Math.max(overdueDays, 0);

    }

    public double calculateFine(Member member) {
        int overdueDays = calculateOverduedays(member);
        if (overdueDays > 0) {
            return overdueDays * member.getFineRate();
        } else {
            return 0.0; 
        }
    }

    public String toString() {
        return "Transaction ID: " + transactionID + ", Member ID: " + memberID + ", Book ID: " + bookID + ", Fine: " + fineAmount + ", Fine Paid: " + isFineApplied;
    }
}
