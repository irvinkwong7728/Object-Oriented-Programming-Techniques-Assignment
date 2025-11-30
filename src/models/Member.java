package models;

public abstract class Member extends Person{
    private int memberID;
    private int borrowedCount;
    private String role;

    // Constructor
    public Member(int memberID, String name, String password, String phoneNum, String email , String role , int borrowedCount) {
        super(name , password , phoneNum , email);
        
        this.memberID = memberID;
        this.role = role;
        this.borrowedCount = borrowedCount;
        
    }
    // Default constructor
    public Member() {
    }

    // Getters and Setters
    public int getMemberId() {
        return memberID;
    }

    public int getBorrowedCount() {
        return borrowedCount;
    }

    public String getRole() {
        return role;
    }

    public void setBorrowedCount(int borrowedCount) {
        this.borrowedCount = borrowedCount;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public abstract int getBorrowLimit();
    public abstract int getMaxBorrowDays();
    public abstract double getFineRate();

    public boolean calculateBorrowCount() {
        if(getBorrowedCount() == getBorrowLimit()){
            return true;
        } else {
            return false;
        }
    } 
        
    public String toString(){
        return memberID + "," + super.toString() + "," + role + "," + borrowedCount;
    }
}

   

