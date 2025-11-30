package models;

public class Staff extends Person{
    private int staffID;

    public Staff(int staffID, String name, String password, String phoneNum, String email) {
        super(name , password , phoneNum , email);
        
        this.staffID = staffID;
    }

    public Staff() {
    }

    public int getStaffId() {
        return staffID;
    }
    
    public String toString(){
        return staffID + "," + super.toString();
    }
}
