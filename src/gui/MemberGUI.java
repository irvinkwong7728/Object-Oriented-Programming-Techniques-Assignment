package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import Driver.Main;
import data.*;
import models.*;

public class MemberGUI {
    private Member currentUser;
    private MemberDAO memberdao;
    private BookingDAO bookingDAO = new BookingDAO();
    private String[] roleOptions = {"Student" , "Faculty"};
    private String [] bookGenres = {"Science Fiction", "Philosophy", "History", "Psychology", "Business", "Finance", "Health", "Travel"};

    public MemberGUI(MemberDAO memberdao) {
        this.memberdao = memberdao;
    }

    public void exitButton(JFrame frame , JFrame previousFrame, int x , int y){
        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(x, y, 80, 30);
        frame.add(exitButton,  BorderLayout.CENTER);
        exitButton.addActionListener( e-> {
            if(previousFrame == null){
                frame.dispose();
                new Main().menuGUI();
            } else {
                frame.dispose();
                previousFrame.setVisible(true);
            }
        });
    }
    
    public void memberSignUpGui(){
        JFrame signUpFrame = new JFrame("MEMBER SIGN UP");
        signUpFrame.setSize(500,500);
        signUpFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        signUpFrame.setLayout(null);
        signUpFrame.setLocationRelativeTo(null);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(50,20,50,20);
        signUpFrame.add(nameLabel);

        JTextField nameBox = new JTextField();
        nameBox.setBounds(180,20,200,20);
        signUpFrame.add(nameBox);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50,50,80,20);
        signUpFrame.add(passwordLabel);

        JPasswordField password_box = new JPasswordField();
        password_box.setBounds(180,50,200,20);
        signUpFrame.add(password_box);

        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setBounds(50, 80, 80, 20);
        signUpFrame.add(roleLabel);

        JComboBox<String> roleBox = new JComboBox<>(roleOptions);
        roleBox.setBounds(180, 80, 200, 20);
        signUpFrame.add(roleBox);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50,110,80,20);
        signUpFrame.add(emailLabel);

        JTextField email_box = new JTextField();
        email_box.setBounds(180,110,200,20);
        signUpFrame.add(email_box);

        JLabel PhoneLabel = new JLabel("Phone Number:");
        PhoneLabel.setBounds(50,140,120,20);
        signUpFrame.add(PhoneLabel);

        JTextField Phone_box = new JTextField();
        Phone_box.setBounds(180,140,200,20);
        signUpFrame.add(Phone_box);

        JButton signupButton = new JButton("Sign Up");
        signupButton.setBounds(200,180,80,30);
        signUpFrame.add(signupButton);

        exitButton(signUpFrame , null , 200 , 300);

        signupButton.addActionListener(e -> {
                String name = nameBox.getText();
                String role = (String) roleBox.getSelectedItem();
                String phoneNum = Phone_box.getText();
                String email = email_box.getText();
                char [] passwordch = password_box.getPassword();
                String password = new String(passwordch);
            
                int memberID = MemberDAO.generateMemberID();
                Member newUser = null;
                if (role.equals("Student")) {
                    newUser = new Student(memberID, name, password, phoneNum, email , role , 0);
                } else if (role.equals("Faculty")) {
                    newUser = new Faculty(memberID, name, password, phoneNum, email , role , 0);
                }

                if(name.isEmpty() || password.isEmpty() || email.isEmpty() || phoneNum.isEmpty() ){
                    JOptionPane.showMessageDialog(null, "Please fill in all the fields.");
                } else if (memberdao.memberEmailExists(email)) {
                    JOptionPane.showMessageDialog(null, "Email already exist.");
                } else if (Person.validatePerson(newUser)) {
                    memberdao.createMember(newUser);
                    JOptionPane.showMessageDialog(signUpFrame, "Sign Up Successful! \nYour Member ID is: " + newUser.getMemberId());
                    signUpFrame.dispose(); 
                    new Main().menuGUI();
                }
        });

        signUpFrame.setVisible(true);
}

    public void memberLoginGui(){
        JFrame loginFrame = new JFrame("MEMBER LOG IN");
        loginFrame.setSize(500,500);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(null);
        loginFrame.setLocationRelativeTo(null); 

        JLabel memberIdLogin = new JLabel("Member ID:");
        memberIdLogin.setBounds(50,20,80,20);
        loginFrame.add(memberIdLogin);

        JTextField idField = new JTextField();
        idField.setBounds(150,20,200,20);
        loginFrame.add(idField);

        JLabel memberPassLogin = new JLabel("Password:");
        memberPassLogin.setBounds(50,50,80,20);
        loginFrame.add(memberPassLogin);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(150,50,200,20);
        loginFrame.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(200,100,80,30);
        loginFrame.add(loginButton);

        exitButton(loginFrame , null , 200 , 300);

        loginButton.addActionListener(e -> {
            String memberID = idField.getText();
            char [] passwordch = passwordField.getPassword();
            String password = new String(passwordch);

            this.currentUser = memberdao.loginValidation(memberID , password);

            if (currentUser != null) {
                loginFrame.setVisible(false);
                memberMainGui(loginFrame);
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Invalid Credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        loginFrame.setVisible(true);
        
    }

    public void memberMainGui(JFrame previousFrame){
        JFrame memberFrame = new JFrame("WELCOME");
        memberFrame.setSize(500,500);
        memberFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        memberFrame.setLayout(null);
        memberFrame.setLocationRelativeTo(null);

        JLabel welcomeText = new JLabel("Welcome " + currentUser.getName() + "!");
        welcomeText.setBounds(150,50,250,30);
        welcomeText.setFont(new Font("Arial", Font.BOLD, 20));
        memberFrame.add(welcomeText);

        JButton profileButton = new JButton("Profile");
        profileButton.setBounds(180,100,120,30);
        memberFrame.add(profileButton);

        JButton searchButton = new JButton("Borrow Books");
        searchButton.setBounds(180,140,120,30);
        memberFrame.add(searchButton);

        JButton returnButton = new JButton("Return Books");
        returnButton.setBounds(180,180,120,30);
        memberFrame.add(returnButton);

        JButton bookingButton = new JButton("Book Rooms");
        bookingButton.setBounds(180,220,120,30);
        memberFrame.add(bookingButton);

        exitButton(memberFrame , null , 200 , 400);

        profileButton.addActionListener(e ->{
            memberFrame.setVisible(false);
            memberProfileGui(memberFrame);

        });

        searchButton.addActionListener(e ->{
            memberFrame.setVisible(false);
            bookSearchGui(memberFrame);

        });

        returnButton.addActionListener(e -> {
            memberFrame.setVisible(false);
            bookReturnGui(memberFrame);
        });

        bookingButton.addActionListener(e -> {
            memberFrame.setVisible(false);
            bookRoomGUI(memberFrame);
        });

        memberFrame.setVisible(true);
    }

    public void memberProfileGui(JFrame previousFrame){
        JFrame profileFrame = new JFrame("WELCOME TO YOUR PROFILE");
        profileFrame.setSize(500,500);
        profileFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        profileFrame.setLayout(null);
        profileFrame.setLocationRelativeTo(null);

        JButton updateButton = new JButton("Update Profile");
        updateButton.setBounds(150,100,180,30);
        profileFrame.add(updateButton);

        JButton historyButton = new JButton("View Borrowing History");
        historyButton.setBounds(150,140,180,30);
        profileFrame.add(historyButton);

        JButton deleteButton = new JButton("Delete Account");
        deleteButton.setBounds(150,180,180,30);
        profileFrame.add(deleteButton);

        exitButton(profileFrame , previousFrame , 200 , 300);

        updateButton.addActionListener(e ->{
            profileFrame.setVisible(false);
            updateProfileGui(profileFrame);
        });

        historyButton.addActionListener(e -> {
            profileFrame.setVisible(false);
            historyGui(profileFrame);
        });

        deleteButton.addActionListener(e ->{
            int choice = JOptionPane.showConfirmDialog(null, "ARE YOU SURE YOU WANT TO DELETE YOUR ACCOUNT", "WARNING",JOptionPane.YES_NO_OPTION);
            if(choice == JOptionPane.YES_OPTION){
                memberdao.deleteMember(currentUser);
                new TransactionDAO().deleteTransaction(currentUser);
                profileFrame.setVisible(false);
                new Main().menuGUI();
            }  
        });

        profileFrame.setVisible(true);

    }

    public void updateProfileGui(JFrame previousFrame){
        JFrame updateFrame = new JFrame("WELCOME");
        updateFrame.setSize(500,500);
        updateFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        updateFrame.setLayout(null);
        updateFrame.setLocationRelativeTo(null);

        JLabel IdLabel = new JLabel("Member ID:");
        IdLabel.setBounds(50,20,120,20);
        updateFrame.add(IdLabel);

        JLabel IDfield = new JLabel(String.valueOf(currentUser.getMemberId()));
        IDfield.setBounds(150,20,200,20);
        updateFrame.add(IDfield);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(50,50,120,20);
        updateFrame.add(nameLabel);

        JTextField namefield = new JTextField(currentUser.getName());
        namefield.setBounds(150,50,200,20);
        namefield.setEditable(true);
        updateFrame.add(namefield);

        JLabel roleLabel = new JLabel("Role: ");
        roleLabel.setBounds(50,80,120,20);
        updateFrame.add(roleLabel);

        JLabel rolefield = new JLabel(String.valueOf(currentUser.getRole()));
        rolefield.setBounds(150,80,200,20);
        updateFrame.add(rolefield);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50,110,120,20);
        updateFrame.add(passLabel);

        JPasswordField passfield = new JPasswordField(currentUser.getPassword());
        passfield.setBounds(150,110,200,20);
        passfield.setEditable(true);
        updateFrame.add(passfield);

        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setBounds(50,140,120,20);
        updateFrame.add(phoneLabel);

        JTextField phonefield = new JTextField(currentUser.getPhone());
        phonefield.setBounds(150,140,200,20);
        phonefield.setEditable(true);
        updateFrame.add(phonefield);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50,170,120,20);
        updateFrame.add(emailLabel);

        JTextField emailfield = new JTextField(currentUser.getEmail());
        emailfield.setBounds(150,170,200,20);
        emailfield.setEditable(true);
        updateFrame.add(emailfield);

        JButton updateButton = new JButton("Update");
        updateButton.setBounds(250, 210, 100, 30);
        updateFrame.add(updateButton);

        exitButton(updateFrame , previousFrame , 200 , 300);

        updateButton.addActionListener(e -> {
            char [] passwordch = passfield.getPassword();
            String password = new String(passwordch);

            currentUser.setName(namefield.getText());
            currentUser.setPassword(password);
            currentUser.setPhone(phonefield.getText());
            currentUser.setEmail(emailfield.getText());

            if(currentUser.getName().isEmpty() || currentUser.getPassword().isEmpty() || currentUser.getEmail().isEmpty() || currentUser.getPhone().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please fill in all the fields.");
                } else if (Person.validatePerson(currentUser)) {
                memberdao.updateMember(currentUser);
                JOptionPane.showMessageDialog(updateFrame, "Profile Updated!");
            }
           
        });

        updateFrame.setVisible(true);
    }

    public void historyGui(JFrame previousFrame){ 
        JFrame historyFrame = new JFrame("Borrowing History");
        historyFrame.setSize(800, 400);
        historyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        historyFrame.setLocationRelativeTo(null);
        historyFrame.setLayout(null);

        DefaultTableModel model = new DefaultTableModel(new String[]{"Transaction ID", "Book Title", "Borrow Date", "Return Date"}, 0){
            public boolean isCellEditable(int row, int column) {
                return false; // 
            }
        };

        JTable table = new JTable(model);
        table.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 20, 740, 280);
        historyFrame.add(scrollPane);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        TransactionDAO dao = new TransactionDAO(); 
        List<Transaction> transactions = dao.getBorrowingRecord(currentUser);

        for (Transaction t : transactions) {
            model.addRow(new Object[]{
                t.getTransactionID(),
                t.getBookTitle(),
                sdf.format(t.getBorrowDate()),
                (t.getReturnDate() != null ? sdf.format(t.getReturnDate()) : "Not Returned"),
            });
        }
        exitButton(historyFrame, previousFrame , 350 , 300);
        historyFrame.setVisible(true);
    }
    
    public void bookRoomGUI(JFrame previousFrame) {
        JFrame roomFrame = new JFrame("Room Management");
        roomFrame.setSize(800, 500);
        roomFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        roomFrame.setLayout(null);
        roomFrame.setLocationRelativeTo(null);

        JLabel roomLabel = new JLabel("Room:");
        roomLabel.setBounds(100, 50, 80, 20);
        roomFrame.add(roomLabel);

        List<Room> rooms = new RoomDAO().listRooms();
        String[] roomOptions = new String[rooms.size()];
        for (int i = 0; i < rooms.size(); i++) {
            roomOptions[i] = rooms.get(i).getRoomID() + " - " + rooms.get(i).getRoomType();
        }
        JComboBox<String> roomBox = new JComboBox<>(roomOptions);
        roomBox.setBounds(180, 50, 200, 20);
        roomFrame.add(roomBox);

        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setBounds(100, 80, 80, 20);
        roomFrame.add(dateLabel);

        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        String [] dateOptions = {today.toString(), tomorrow.toString()};

        JComboBox<String> dateBox = new JComboBox<>(dateOptions);
        dateBox.setBounds(180, 80, 200, 20);
        roomFrame.add(dateBox);

        JLabel startTimeLabel = new JLabel("Start Time:");
        startTimeLabel.setBounds(100, 110, 80, 20);
        roomFrame.add(startTimeLabel);

        JComboBox<String> startTimeBox = new JComboBox<>();
        startTimeBox.setBounds(180, 110, 200, 20);
        roomFrame.add(startTimeBox);

        JLabel endTimeLabel = new JLabel("End Time:");
        endTimeLabel.setBounds(100, 140, 80, 20);
        roomFrame.add(endTimeLabel);

        JComboBox<String> endTimeBox = new JComboBox<>();
        endTimeBox.setBounds(180, 140, 200, 20);
        roomFrame.add(endTimeBox);

        loadTimeOptions(startTimeBox, endTimeBox);

        JButton bookBtn = new JButton("Book Room");
        bookBtn.setBounds(200, 200, 160, 30);
        roomFrame.add(bookBtn);

        JButton showBtn = new JButton("Booking Details");
        showBtn.setBounds(200, 1000, 80, 30);
        roomFrame.add(showBtn);

        JLabel cancelLabel = new JLabel("Input BookingID to cancel:");
        cancelLabel.setBounds(500, 200, 300, 20);
        roomFrame.add(cancelLabel);

        JTextField cancelField = new JTextField();
        cancelField.setBounds(500, 230, 200, 20);
        roomFrame.add(cancelField);

        JButton cancelBtn = new JButton("Confirm");
        cancelBtn.setBounds(565, 260, 80, 30);
        roomFrame.add(cancelBtn);

        JLabel updateLabel = new JLabel("Enter Booking ID to update time:");
        updateLabel.setBounds(500, 50, 300, 20);
        roomFrame.add(updateLabel);

        JTextField updateField = new JTextField();
        updateField.setBounds(500, 80, 200, 20);
        roomFrame.add(updateField);

        JComboBox<String> newStartTimeBox = new JComboBox<>(); 
        newStartTimeBox.setBounds(500, 120, 100, 20);
        roomFrame.add(newStartTimeBox);

        JComboBox<String> newEndTimeBox = new JComboBox<>();
        newEndTimeBox.setBounds(620, 120, 100, 20);
        roomFrame.add(newEndTimeBox);

        loadNewTimeOptions(newStartTimeBox, newEndTimeBox);

        JButton updateBtn = new JButton("Update Time");
        updateBtn.setBounds(550, 150, 120, 30);
        roomFrame.add(updateBtn);

        exitButton(roomFrame, previousFrame, 350, 400);

        bookBtn.addActionListener(e -> {
            try {
                String selectedRoomText = (String) roomBox.getSelectedItem();
                String roomID = selectedRoomText.split(" - ")[0]; 
                LocalDate date = LocalDate.parse((String) dateBox.getSelectedItem());
                
                String startTimeStr = (String) startTimeBox.getSelectedItem();
                String endTimeStr = (String) endTimeBox.getSelectedItem();

                LocalTime startTime = LocalTime.parse(startTimeStr);  
                LocalTime endTime = LocalTime.parse(endTimeStr); 
    
                if (startTime == null || endTime == null || startTime.isAfter(endTime) || Duration.between(startTime, endTime).toHours() < 1 || Duration.between(startTime, endTime).toHours() > 2) {
                    JOptionPane.showMessageDialog(null, "Start time must be before End time and between 1 to 2 hours!", "Invalid Time", JOptionPane.ERROR_MESSAGE);
                    return;
                }
    
                Booking newBooking = new Booking(null, roomID, currentUser.getMemberId(), date, startTime, endTime);
                bookingDAO.createBooking(newBooking);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error booking room: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        showBtn.addActionListener(e -> { 
            List<Booking> bookings = bookingDAO.getAllBookings();
        StringBuilder sb = new StringBuilder();
        for (Booking b : bookings) {
            sb.append(b).append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "All Bookings", JOptionPane.INFORMATION_MESSAGE);
        });

        cancelBtn.addActionListener(e -> {   
            String bookingID = cancelField.getText();
        if (bookingID == null || bookingID.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter a Booking ID to cancel.", "Missing Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean success = bookingDAO.cancelBooking(bookingID);
        if (success) {
            JOptionPane.showMessageDialog(null, "Booking cancelled successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Booking ID not found.", "Failure", JOptionPane.ERROR_MESSAGE);
        }
        });
        
        updateBtn.addActionListener(e -> {
            String bookingID = updateField.getText();
            Booking booking = bookingDAO.getBookingById(bookingID);
        
            if (booking == null) {
                JOptionPane.showMessageDialog(null, "Booking not found.");
                return;
            }
        
            try {
                LocalTime newStart = LocalTime.parse((String) newStartTimeBox.getSelectedItem());
                LocalTime newEnd = LocalTime.parse((String) newEndTimeBox.getSelectedItem());
        
                if (newStart.isAfter(newEnd) || Duration.between(newStart, newEnd).toHours() < 1 || Duration.between(newStart, newEnd).toHours() > 2) {
                    JOptionPane.showMessageDialog(null, "Start time must be before end time and between 1–2 hours.");
                    return;
                }
        
                booking.setStartTime(newStart);
                booking.setEndTime(newEnd);
        
                boolean success = bookingDAO.updateBookingTime(booking.getBookingID(), newStart, newEnd); 
                if (success) {
                    JOptionPane.showMessageDialog(null, "Booking time updated.");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to update time.");
                }
        
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        });
        
        roomFrame.setVisible(true);
    }

    private void loadTimeOptions(JComboBox<String> startTimeBox, JComboBox<String> endTimeBox) {
        LocalTime time = LocalTime.of(8, 0);
        while (!time.isAfter(LocalTime.of(17, 0))) {
            startTimeBox.addItem(time.toString());
            endTimeBox.addItem(time.toString());
            time = time.plusMinutes(30);
        }
    }

    private void loadNewTimeOptions(JComboBox<String> newStartTimeBox, JComboBox<String> newEndTimeBox) {
        LocalTime time = LocalTime.of(8, 0);
        while (!time.isAfter(LocalTime.of(17, 0))) {
            newStartTimeBox.addItem(time.toString());
            newEndTimeBox.addItem(time.toString());
            time = time.plusMinutes(30);
        }
    }
 
    public void bookSearchGui(JFrame previousFrame) {

        JFrame searchFrame = new JFrame("SEARCH BOOKS");
        searchFrame.setSize(500, 600);
        searchFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        searchFrame.setLayout(null);
        searchFrame.setLocationRelativeTo(null);

        JLabel genreLabel = new JLabel("Genre:");
        genreLabel.setBounds(50, 20, 80, 20);
        searchFrame.add(genreLabel);

        JComboBox<String> genreBox = new JComboBox<>(bookGenres);
        genreBox.setBounds(180, 20, 200, 20);
        searchFrame.add(genreBox);

        JLabel titleLabel = new JLabel("Title:");
        titleLabel.setBounds(50, 50, 80, 20);
        searchFrame.add(titleLabel);

        JTextField titleField = new JTextField();
        titleField.setBounds(180, 50, 200, 20);
        searchFrame.add(titleField);

        JLabel authorLabel = new JLabel("Author:");
        authorLabel.setBounds(50, 80, 80, 20);
        searchFrame.add(authorLabel);

        JTextField authorField = new JTextField();
        authorField.setBounds(180, 80, 200, 20);
        searchFrame.add(authorField);

        JLabel yearLabel = new JLabel("Publication Year:");
        yearLabel.setBounds(50, 110, 120, 20);
        searchFrame.add(yearLabel);

        JTextField yearField = new JTextField();
        yearField.setBounds(180, 110, 200, 20);
        searchFrame.add(yearField);

        JButton searchButton = new JButton("Search");
        searchButton.setBounds(200, 140, 80, 30);
        searchFrame.add(searchButton);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> bookList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(bookList);
        scrollPane.setBounds(50, 180, 380, 200);
        searchFrame.add(scrollPane); 

        JButton borrowButton = new JButton("Borrow");
        borrowButton.setBounds(200, 400, 80, 30);
        searchFrame.add(borrowButton);

        exitButton(searchFrame , previousFrame, 200, 500);

        searchButton.addActionListener(e -> {
            String genre = (String) genreBox.getSelectedItem();
            String title = titleField.getText();
            String author = authorField.getText();
            String year = yearField.getText();
        
            ArrayList<Book> results = BookDAO.searchBooks(genre, title, author, year);
            
            listModel.clear();
            
            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(searchFrame, "No books found matching your criteria.", "No Results", JOptionPane.INFORMATION_MESSAGE);
            } else {
                
                for (Book book : results) {
                    String bookInfo =  " Title: " + book.getTitle();
                    listModel.addElement(bookInfo);
                }
            }
        });

        bookList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && bookList.getSelectedIndex() != -1) {
                borrowButton.setEnabled(true);
            }
        });

        borrowButton.addActionListener(e -> {
            int selectedIndex = bookList.getSelectedIndex();
            if (selectedIndex != -1) {
                ArrayList<Book> results = BookDAO.searchBooks(
                    (String) genreBox.getSelectedItem(),
                    titleField.getText(), 
                    authorField.getText(), 
                    yearField.getText()
                );

            if (selectedIndex >= 0 && selectedIndex < results.size()) {
                Book selectedBookObj = results.get(selectedIndex);

                TransactionDAO transactionDAO = new TransactionDAO();
                List<Transaction> studentTransactions = transactionDAO.getBorrowingRecord(currentUser);

                for (Transaction t : studentTransactions) {
                    if (t.getReturnDate() != null && !t.isFineApplied() && t.calculateFine(currentUser) > 0) {
                    JOptionPane.showMessageDialog(null, "You have unpaid fines. Please settle them before borrowing.");
                    return; 
                    }
                }

                if(currentUser.calculateBorrowCount()){
                    JOptionPane.showMessageDialog(searchFrame, "You have reached your borrow limit.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String bookID = selectedBookObj.getBookID();
                BookDAO bookDAO = new BookDAO();
                bookDAO.updateBookStatus(bookID, false);

                new MemberDAO().updateBorrowedCount(currentUser);
                
                int transactionID = new Random().nextInt(9999);
                int borrowID = new Random().nextInt(9999);
                Date borrowDate = new Date();
                Date returnDate = null;

                //Save transaction
                Transaction transaction = new Transaction(transactionID, borrowID, currentUser.getMemberId(), bookID, borrowDate, returnDate, false ,0.0);
                transactionDAO.saveTransaction(transaction);
                
                //Update text files
                listModel.removeElementAt(selectedIndex);

                JOptionPane.showMessageDialog(searchFrame, "Book borrowed successfully! Borrow ID: " + borrowID, "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }       
    });
    
        searchFrame.setVisible(true);
    }

    public void bookReturnGui(JFrame previousFrame) {
        JFrame returnFrame = new JFrame("RETURN BOOK");
        returnFrame.setSize(500, 600);
        returnFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        returnFrame.setLayout(null);
        returnFrame.setLocationRelativeTo(null);

        JLabel textLabel = new JLabel("Select the Book you want to return");
        textLabel.setBounds(150, 20, 200, 20);
        returnFrame.add(textLabel);
    
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> bookList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(bookList);
        scrollPane.setBounds(50, 100, 380, 200);
        returnFrame.add(scrollPane);
    
        JButton returnButton = new JButton("Return Book");
        returnButton.setBounds(180, 320, 120, 30);
        returnButton.setEnabled(false);
        returnFrame.add(returnButton);
    
        exitButton(returnFrame , previousFrame, 200, 500);
    
        ArrayList<Transaction> validTransactions = new ArrayList<>();

        listModel.clear();
        validTransactions.clear();
                
        TransactionDAO transactionDAO = new TransactionDAO();
        List<Transaction> transactions = transactionDAO.getBorrowingRecord(currentUser);
        for (Transaction t : transactions){
            if (t.getReturnDate() == null){
                String bookTitle = t.getBookTitle();
                String display = "Transaction #" + t.getTransactionID() + " - " + bookTitle + " | Borrowed: " + t.getBorrowDate();
                listModel.addElement(display);
                validTransactions.add(t);
                System.out.println(">> Match found and added: " + display);
            }
        }                       
        
        bookList.addListSelectionListener(e -> {
            returnButton.setEnabled(!bookList.isSelectionEmpty());
        });
        
        returnButton.addActionListener(e ->{
            int selectedIndex = bookList.getSelectedIndex();
            if (selectedIndex != -1){
                Transaction selectedTransaction = validTransactions.get(selectedIndex);
                String bookID = selectedTransaction.getBookID();
                int transactionID = selectedTransaction.getTransactionID();
        
                new BookDAO().updateBookStatus(bookID, true);

                transactionDAO.markTransactionAsReturned(transactionID);
                new MemberDAO().updateReturnedCount(currentUser);
        
                listModel.remove(selectedIndex);
                validTransactions.remove(selectedIndex);
                
                JOptionPane.showMessageDialog(returnFrame, "Book returned successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            }
        });

        returnFrame.setVisible(true);
    }
    
}

