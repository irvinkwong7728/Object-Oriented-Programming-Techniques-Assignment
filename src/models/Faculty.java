package models;

public class Faculty extends Member {
    public Faculty(int memberID, String name, String password, String phoneNum, String email , String role , int borrowedCount) {
        super(memberID, name, password, phoneNum, email , "Faculty" , borrowedCount);
    }

    @Override
    public int getBorrowLimit() {
        return 6;
    }

    @Override
    public int getMaxBorrowDays() {
        return 14;
    }

    @Override
    public double getFineRate() {
        return 1.0;  
    }
}

