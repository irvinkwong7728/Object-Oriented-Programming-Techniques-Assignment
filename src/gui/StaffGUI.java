package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.util.List;
import java.awt.*;
import java.text.SimpleDateFormat;
import Driver.Main;
import data.*;
import models.*;

public class StaffGUI {
    private Staff currentStaff;
    private StaffDAO staffdao;
    private String [] bookGenres = {"Science Fiction", "Philosophy", "History", "Psychology", "Business", "Finance", "Health", "Travel"};

    public StaffGUI(StaffDAO staffdao) {
        this.staffdao = staffdao;
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
    
    public void staffSignUpGui(){
        JFrame signUpFrame = new JFrame("STAFF SIGN UP");
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

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50,80,80,20);
        signUpFrame.add(emailLabel);

        JTextField emailBox = new JTextField();
        emailBox.setBounds(180,80,200,20);
        signUpFrame.add(emailBox);

        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setBounds(50,110,120,20);
        signUpFrame.add(phoneLabel);

        JTextField phoneBox = new JTextField();
        phoneBox.setBounds(180,110,200,20);
        signUpFrame.add(phoneBox);

        JButton signupButton = new JButton("Sign Up");
        signupButton.setBounds(200,150,80,30);
        signUpFrame.add(signupButton);

        exitButton(signUpFrame , null , 200 , 300);

        signupButton.addActionListener(e -> {
                String name = nameBox.getText();
                String phoneNum = phoneBox.getText();
                String email = emailBox.getText();
                char [] passwordch = password_box.getPassword();
                String password = new String(passwordch);
            
                int staffID = StaffDAO.generateStaffID();
                Staff newStaff = new Staff(staffID, name, password, phoneNum, email);

                if(name.isEmpty() || password.isEmpty() || email.isEmpty() || phoneNum.isEmpty() ){
                    JOptionPane.showMessageDialog(null, "Please fill in all the fields.");
                } else if (staffdao.staffEmailExists(email)) {
                    JOptionPane.showMessageDialog(null, "Email already exist.");
                } else if (Person.validatePerson(newStaff)) {
                    staffdao.createStaff(newStaff);
                    JOptionPane.showMessageDialog(signUpFrame, "Sign Up Successful! \nYour Member ID is: " + newStaff.getStaffId());
                    signUpFrame.dispose(); 
                    new Main().menuGUI();
                }
                   
        });

        signUpFrame.setVisible(true);
}

    public void staffLoginGui(){
        JFrame loginFrame = new JFrame("STAFF LOGIN");
        loginFrame.setSize(500,500);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(null);
        loginFrame.setLocationRelativeTo(null);

        JLabel staffIdLogin = new JLabel("StaffID:");
        staffIdLogin.setBounds(50,20,80,20);
        loginFrame.add(staffIdLogin);

        JTextField IdField = new JTextField();
        IdField.setBounds(150,20,200,20);
        loginFrame.add(IdField);

        JLabel staffPassLogin = new JLabel("Password:");
        staffPassLogin.setBounds(50,50,80,20);
        loginFrame.add(staffPassLogin);

        JPasswordField passwordfield = new JPasswordField();
        passwordfield.setBounds(150,50,200,20);
        loginFrame.add(passwordfield);

        JButton LoginButton = new JButton("Login");
        LoginButton.setBounds(200,100,80,30);
        loginFrame.add(LoginButton);

        exitButton(loginFrame, null , 200, 300);

        LoginButton.addActionListener(e -> {
            String StaffID = IdField.getText();
            char [] passwordch = passwordfield.getPassword();
            String password = new String(passwordch);

            currentStaff = new StaffDAO().loginValidation(StaffID, password);

            if (currentStaff != null) {
                loginFrame.setVisible(false);
                staffMainGui();
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Invalid Credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        loginFrame.setVisible(true);
    
    }

    public void staffMainGui(){
        JFrame staffFrame = new JFrame("WELCOME");
        staffFrame.setSize(500,500);
        staffFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        staffFrame.setLayout(null);
        staffFrame.setLocationRelativeTo(null);

        JLabel welcomeText = new JLabel("Welcome " + currentStaff.getName() + "!");
        welcomeText.setBounds(180,50,250,30);
        welcomeText.setFont(new Font("Arial", Font.BOLD, 20));
        staffFrame.add(welcomeText);

        JButton bookButton = new JButton("Manage Books");
        bookButton.setBounds(180,100,150,30);
        staffFrame.add(bookButton);

        JButton roomButton = new JButton("Manage Rooms");
        roomButton.setBounds(180,140,150,30);
        staffFrame.add(roomButton);

        exitButton(staffFrame, null , 200 , 300);

        bookButton.addActionListener(e ->{
            staffFrame.setVisible(false);
            manageBookGui(staffFrame);
        });

        roomButton.addActionListener(e ->{
            staffFrame.setVisible(false);
            manageRoomGui(staffFrame);
        });

        staffFrame.setVisible(true);
        
    }

    public void manageBookGui(JFrame previousFrame){
        JFrame bookFrame = new JFrame("WELCOME");
        bookFrame.setSize(500,500);
        bookFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        bookFrame.setLayout(null);
        bookFrame.setLocationRelativeTo(null);

        JButton addBooks = new JButton("Add Books");
        addBooks.setBounds(150,100,180,30);
        bookFrame.add(addBooks);

        JButton removeBooks = new JButton("Remove Books");
        removeBooks.setBounds(150,140,180,30);
        bookFrame.add(removeBooks);

        JButton updateBooks = new JButton("Update Book Details");
        updateBooks.setBounds(150,180,180,30);
        bookFrame.add(updateBooks);

        JButton viewBooks = new JButton("View Overdue Books");
        viewBooks.setBounds(150,220,180,30);
        bookFrame.add(viewBooks);

        exitButton(bookFrame, previousFrame , 200 , 300);

        addBooks.addActionListener(e ->{
            bookFrame.setVisible(false);
            addNewBookGui(bookFrame);
        });

        removeBooks.addActionListener(e ->{
            bookFrame.setVisible(false);
            removeOldBooksGui(bookFrame);
        });

        updateBooks.addActionListener(e ->{
            bookFrame.setVisible(false);
            updateBookDetailsGui(bookFrame);
        });

        viewBooks.addActionListener(e ->{
            bookFrame.setVisible(false);
            viewOverdueBooksGui(bookFrame);
        });

        bookFrame.setVisible(true);
    }

    public void manageRoomGui(JFrame previousFrame){
        JFrame roomFrame = new JFrame("WELCOME");
        roomFrame.setSize(500,500);
        roomFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        roomFrame.setLayout(null);
        roomFrame.setLocationRelativeTo(null);

        JButton addRooms = new JButton("Add Rooms");
        addRooms.setBounds(180,100,150,30);
        roomFrame.add(addRooms);

        JButton removeRooms = new JButton("Delete Rooms");
        removeRooms.setBounds(180,140,150,30);
        roomFrame.add(removeRooms);

        exitButton(roomFrame, previousFrame , 200 , 300);

        addRooms.addActionListener(e ->{
            roomFrame.setVisible(false);
            addRoomGui(roomFrame);
        });

        removeRooms.addActionListener(e ->{
            roomFrame.setVisible(false);
            deleteRoomGui(roomFrame);
        });

        roomFrame.setVisible(true);
    }
    
    public void addRoomGui(JFrame previousFrame){
        JFrame addRoomFrame = new JFrame("Room Management");
        addRoomFrame.setSize(500, 500);
        addRoomFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addRoomFrame.setLayout(null);
        addRoomFrame.setLocationRelativeTo(null);

        JLabel roomTypeLabel = new JLabel("Room Type:");
        roomTypeLabel.setBounds(50, 50, 100, 20);
        addRoomFrame.add(roomTypeLabel);

        JTextField roomTypeField = new JTextField();
        roomTypeField.setBounds(180, 50, 200, 20);
        addRoomFrame.add(roomTypeField);

        JButton createRoomButton = new JButton("Create Room");
        createRoomButton.setBounds(200, 150, 120, 30);
        addRoomFrame.add(createRoomButton);

        exitButton(addRoomFrame, previousFrame, 200, 380);

        createRoomButton.addActionListener(e -> {
            String roomID = new RoomDAO().generateRoomID(); 
            String roomType = roomTypeField.getText();

            if (roomType.isEmpty()) {
                JOptionPane.showMessageDialog(addRoomFrame, "Room Type cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                new RoomDAO().addRoom(roomID, roomType);
                roomTypeField.setText("");
            }
        });

        addRoomFrame.setVisible(true);
    }

    public void deleteRoomGui(JFrame previousFrame){
        JFrame deleteRoomFrame = new JFrame("Room Management");
        deleteRoomFrame.setSize(500, 500);
        deleteRoomFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        deleteRoomFrame.setLayout(null);
        deleteRoomFrame.setLocationRelativeTo(null);

        JLabel deleteRoomLabel = new JLabel("Room ID to Delete:");
        deleteRoomLabel.setBounds(50, 150, 150, 20);
        deleteRoomFrame.add(deleteRoomLabel);

        JTextField deleteRoomField = new JTextField();
        deleteRoomField.setBounds(180, 150, 200, 20);
        deleteRoomFrame.add(deleteRoomField);

        JButton deleteRoomButton = new JButton("Delete Room");
        deleteRoomButton.setBounds(200, 190, 120, 30);
        deleteRoomFrame.add(deleteRoomButton);

        exitButton(deleteRoomFrame, previousFrame, 200, 380);

        deleteRoomButton.addActionListener(e -> {
            String roomID = deleteRoomField.getText();

            if (roomID.isEmpty()) {
                JOptionPane.showMessageDialog(deleteRoomFrame, "Please enter a room ID to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                new RoomDAO().deleteRoom(roomID);
                deleteRoomField.setText("");
            }
        });
        
        deleteRoomFrame.setVisible(true);
    }

    public void updateBookDetailsGui(JFrame previousFrame) {
        JFrame updateFrame = new JFrame("UPDATE BOOK DETAILS");
        updateFrame.setSize(500, 400);
        updateFrame.setLayout(null);
        updateFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        updateFrame.setLocationRelativeTo(null);
    
        JLabel idLabel = new JLabel("Book ID:");
        idLabel.setBounds(50, 20, 100, 20);
        updateFrame.add(idLabel);
    
        JTextField idField = new JTextField();
        idField.setBounds(180, 20, 200, 20);
        updateFrame.add(idField);
    
        JLabel genreLabel = new JLabel("New Genre:");
        genreLabel.setBounds(50, 60, 100, 20);
        updateFrame.add(genreLabel);
    
        JComboBox<String> genreBox = new JComboBox<>(bookGenres);
        genreBox.setBounds(180, 60, 200, 20);
        updateFrame.add(genreBox);
    
        JLabel titleLabel = new JLabel("New Title:");
        titleLabel.setBounds(50, 100, 100, 20);
        updateFrame.add(titleLabel);
    
        JTextField titleField = new JTextField();
        titleField.setBounds(180, 100, 200, 20);
        updateFrame.add(titleField);
    
        JLabel authorLabel = new JLabel("New Author:");
        authorLabel.setBounds(50, 140, 100, 20);
        updateFrame.add(authorLabel);
    
        JTextField authorField = new JTextField();
        authorField.setBounds(180, 140, 200, 20);
        updateFrame.add(authorField);
    
        JLabel yearLabel = new JLabel("New Publication Year:");
        yearLabel.setBounds(50, 180, 150, 20);
        updateFrame.add(yearLabel);
    
        JTextField yearField = new JTextField();
        yearField.setBounds(180, 180, 200, 20);
        updateFrame.add(yearField);
    
        JButton updateButton = new JButton("Update");
        updateButton.setBounds(180, 220, 100, 30);
        updateFrame.add(updateButton);
    
        exitButton(updateFrame, previousFrame, 180, 270);
    
        updateButton.addActionListener(e -> {
            String bookID = idField.getText().trim();
            String genre = (String) genreBox.getSelectedItem();
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String yearText = yearField.getText().trim();
    
            if (bookID.isEmpty() || title.isEmpty() || author.isEmpty() || yearText.isEmpty()) {
                JOptionPane.showMessageDialog(updateFrame, "Please fill in all fields.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
    
            try {
                int year = Integer.parseInt(yearText);
    
                new BookDAO().updateBookDetails(bookID, genre, title, author, year);
                JOptionPane.showMessageDialog(updateFrame, "Book details updated successfully.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(updateFrame, "ID and Year must be numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        updateFrame.setVisible(true);
    }

    public void addNewBookGui(JFrame previousFrame) {
        JFrame addFrame = new JFrame("ADD NEW BOOK");
        addFrame.setSize(500, 500);
        addFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addFrame.setLayout(null);
        addFrame.setLocationRelativeTo(null);

        JLabel genreLabel = new JLabel("Genre:");
        genreLabel.setBounds(50, 20, 80, 20);
        addFrame.add(genreLabel);

        JComboBox<String> genreBox = new JComboBox<>(bookGenres);
        genreBox.setBounds(180, 20, 200, 20);
        addFrame.add(genreBox);

        JLabel titleLabel = new JLabel("Title:");
        titleLabel.setBounds(50, 50, 80, 20);
        addFrame.add(titleLabel);

        JTextField titleField = new JTextField();
        titleField.setBounds(180, 50, 200, 20);
        addFrame.add(titleField);

        JLabel authorLabel = new JLabel("Author:");
        authorLabel.setBounds(50, 80, 80, 20);
        addFrame.add(authorLabel);

        JTextField authorField = new JTextField();
        authorField.setBounds(180, 80, 200, 20);
        addFrame.add(authorField);

        JLabel yearLabel = new JLabel("Publication Year:");
        yearLabel.setBounds(50, 110, 120, 20);
        addFrame.add(yearLabel);

        JTextField yearField = new JTextField();
        yearField.setBounds(180, 110, 200, 20);
        addFrame.add(yearField);

        JButton addButton = new JButton("Add Book");
        addButton.setBounds(200, 150, 100, 30);
        addFrame.add(addButton);

        exitButton(addFrame , previousFrame , 200, 250);

        addButton.addActionListener(e -> {
            String genre = (String) genreBox.getSelectedItem();
            String title = titleField.getText();
            String author = authorField.getText();
            String year = yearField.getText();

            if (genre == null || title.isEmpty() || author.isEmpty() || year.isEmpty()) {
                JOptionPane.showMessageDialog(addFrame, "Please fill in all fields!", "Missing Information", JOptionPane.WARNING_MESSAGE);
                return;
            } else if (!author.matches("[a-zA-Z\\s]+")){
                JOptionPane.showMessageDialog(addFrame, "Author name cannot be numbers", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int yearInt = Integer.parseInt(year);

                new BookDAO().addBook(genre, title, author, yearInt);
                titleField.setText("");
                authorField.setText("");
                yearField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(addFrame, "Years Only! Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        addFrame.setVisible(true);
    }

    public void removeOldBooksGui(JFrame previousFrame) {
        JFrame removeFrame = new JFrame("REMOVE OLD BOOKS");
        removeFrame.setSize(500, 500);
        removeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        removeFrame.setLayout(null);
        removeFrame.setLocationRelativeTo(null);

        JLabel yearLabel = new JLabel("Remove books before year:");
        yearLabel.setBounds(50, 20, 200, 20);
        removeFrame.add(yearLabel);

        JTextField yearField = new JTextField();
        yearField.setBounds(250, 20, 100, 20);
        removeFrame.add(yearField);

        JButton removeButton = new JButton("Remove");
        removeButton.setBounds(200, 60, 100, 30);
        removeFrame.add(removeButton);

        exitButton(removeFrame , previousFrame, 200, 250);

        removeButton.addActionListener(e -> {
            String yearThreshold = yearField.getText();
            if (yearThreshold.isEmpty()) {
                JOptionPane.showMessageDialog(removeFrame, "Please enter a year.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } else {
                new BookDAO().removeOldBooks(Integer.parseInt(yearThreshold));
                JOptionPane.showMessageDialog(removeFrame, "Books before " + yearThreshold + " have been removed.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        removeFrame.setVisible(true);
    }

    public void viewOverdueBooksGui(JFrame previousFrame) {
        JFrame overdueframe = new JFrame("Overdue Books");
        overdueframe.setSize(800, 400);
        overdueframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        overdueframe.setLocationRelativeTo(null);
        overdueframe.setLayout(null);
    
        DefaultTableModel model = new DefaultTableModel(new String[]{"Member ID", "Book Title", "Borrow Date", "Return Date", "Overdue Days"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    
        JTable table = new JTable(model);
        table.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 20, 740, 180);
        overdueframe.add(scrollPane);
    
        JButton applyFineButton = new JButton("Apply Fine");
        applyFineButton.setBounds(350, 250, 120, 30);
        overdueframe.add(applyFineButton);
    
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        TransactionDAO dao = new TransactionDAO();
        List<Transaction> transactions = dao.getAllTransactions();
    
        for (Transaction t : transactions) {
            if (t.isFineApplied() || t.getReturnDate() == null) 
            continue;
    
            Member member = new MemberDAO().getMemberByID(t.getMemberID());
            int overdueDays = t.calculateOverduedays(member);
            if (overdueDays <= 0) continue;
    
            model.addRow(new Object[]{
                t.getMemberID(),
                t.getBookTitle(),
                sdf.format(t.getBorrowDate()),
                sdf.format(t.getReturnDate()),
                overdueDays
            });
        }
    
        applyFineButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(overdueframe, "Please select a row first.");
                return;
            }
    
            int memberId = (int) model.getValueAt(selectedRow, 0);
            String bookTitle = (String) model.getValueAt(selectedRow, 1);
    
            Transaction selectedTransaction = null;
            for (Transaction t : transactions) {
                if (t.getMemberID() == memberId && t.getBookTitle().equals(bookTitle)) {
                    selectedTransaction = t;
                    break;
                }
            }
    
            if (selectedTransaction != null) {
                Member member = new MemberDAO().getMemberByID(memberId);
                selectedTransaction.applyFine(member);
                dao.updateFineStatus(selectedTransaction);
    
                JOptionPane.showMessageDialog(overdueframe, "Fine of RM" + selectedTransaction.getFineAmount() + " applied to member " + memberId);
                model.removeRow(selectedRow);
            }
        });
    
        exitButton(overdueframe, previousFrame, 350, 300);
        overdueframe.setVisible(true);
    }
    



}


