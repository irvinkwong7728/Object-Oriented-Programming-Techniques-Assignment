package Driver;

import javax.swing.*;

import data.MemberDAO;
import data.StaffDAO;
import gui.MemberGUI;
import gui.StaffGUI;

import java.awt.Font;

public class Main {
    private MemberDAO memberdao;
    private StaffDAO staffdao;

    public Main() {
        memberdao = new MemberDAO();
        staffdao = new StaffDAO();
    }

    public void menuGUI() {
        JFrame menuFrame = new JFrame("WELCOME TO UNIVERSITY LIBRARY");
        menuFrame.setSize(800,500);
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setLayout(null);
        menuFrame.setLocationRelativeTo(null);
        
        JLabel titleLabel = new JLabel("TAR Library");
        titleLabel.setBounds(320,20,300,30);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 28));
        menuFrame.add(titleLabel);

        JButton memberLogin = new JButton("Member Login");
        memberLogin.setBounds(320,100,150,30);
        menuFrame.add(memberLogin);
        
        JButton staffLogin = new JButton("Staff Login");
        staffLogin.setBounds(320,140,150,30);
        menuFrame.add(staffLogin);

        JButton newSignUp = new JButton("New? Sign Up Here");
        newSignUp.setBounds(320,180,150,30);
        menuFrame.add(newSignUp);
        
        memberLogin.addActionListener(e -> {
            MemberGUI gui = new MemberGUI(memberdao);
            gui.memberLoginGui();
            menuFrame.setVisible(false);
        });

        staffLogin.addActionListener(e -> {
            StaffGUI gui = new StaffGUI(staffdao);
            gui.staffLoginGui();
            menuFrame.setVisible(false);
        });

        newSignUp.addActionListener(e -> {
            staffOrMember();
            menuFrame.setVisible(false);
        });

        menuFrame.setVisible(true);
    }

    public void staffOrMember(){
        JFrame optionFrame = new JFrame("WELCOME TO UNIVERSITY LIBRARY");
        optionFrame.setSize(500,500);
        optionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        optionFrame.setLayout(null);
        optionFrame.setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Sign Up As");
        titleLabel.setBounds(180,20,300,30);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 28));
        optionFrame.add(titleLabel);

        JButton memberButton = new JButton("Member");
        memberButton.setBounds(180,150,150,30);
        optionFrame.add(memberButton);

        JButton staffButton = new JButton("Staff");
        staffButton.setBounds(180,200,150,30);
        optionFrame.add(staffButton);

        memberButton.addActionListener(e -> {
            MemberGUI gui = new MemberGUI(memberdao);
            gui.memberSignUpGui();
            optionFrame.setVisible(false);
        });

        staffButton.addActionListener(e -> {
            StaffGUI gui = new StaffGUI(staffdao);
            gui.staffSignUpGui();
            optionFrame.setVisible(false);
        });

        optionFrame.setVisible(true);
    }
    
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            Main Menu = new Main();
            Menu.menuGUI();
        });
    }

}

