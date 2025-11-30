package models;

import javax.swing.JOptionPane;

public class Person {
    private String name;
    private String password;
    private String phoneNum;
    private String email;

    public Person(String name, String password, String phoneNum, String email) {
        this.name = name;
        this.password = password;
        this.phoneNum = phoneNum;
        this.email = email;
    }

    // Default constructor
    public Person() {
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
    
    public String getEmail(){
        return email;
    }
    public String getPhone() {
        return phoneNum;
    }

    public void setName(String name) {
        this.name = name;
        
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phoneNum){
        this.phoneNum = phoneNum;
        
    }

    public void setEmail(String email){
        this.email = email;
              
    }

    public String toString(){
        return name + "," + password + "," + phoneNum + "," +  email;
    }

    public static boolean validatePerson(Person person){
        StringBuilder errorMessages = new StringBuilder();
        String name = person.name;
        String password = person.password;
        String phoneNum = person.phoneNum;
        String email = person.email;

        if (name.length() < 4 ){
            errorMessages.append("Name cannot be less than 5 characters!\n");
        }

        if (!name.matches("[a-zA-Z\\s]+")){
            errorMessages.append("Name cannot contain numbers!\n");
        }

        if (password.length() < 8){
            errorMessages.append("Password cannot be less than 8 characters!\n");
        }

        if (!phoneNum.matches("^01[0-9]{8,9}$")){
            errorMessages.append("Invalid Phone Number!\n");
        }

        if (!email.matches("^[a-z0-9+_.-]+@[a-z]+.com")){
            errorMessages.append("Invalid Email!\n");
        }

        if(errorMessages.length() > 0){
            JOptionPane.showMessageDialog(null, errorMessages.toString());
            return false;
        } else {
            return true;
        }
    }
    
}
