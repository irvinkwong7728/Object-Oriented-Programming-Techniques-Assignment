package models;

public class Student extends Member {
    public Student(int memberID, String name, String password, String phoneNum, String email , String role , int borrowedCount) {
        super(memberID, name, password, phoneNum, email , "Student" , borrowedCount);
    }

    @Override
    public int getBorrowLimit() {
        return 3;
    }

    @Override
    public int getMaxBorrowDays() {
        return 7;
    }

    @Override
    public double getFineRate() {
        return 2.0;  
    }
}

