package org.example.forms;

import org.example.Helpers.ConfirmDialog;
import org.example.Models.Users;

import javax.swing.*;


import static org.example.Helpers.LoadersApi.usersDAO;

public class SignUp extends JFrame {


    private JPanel panelSignup;
    private JTextField txtuserName;
    private JTextField txtEmail;
    private JPasswordField txtPass;
    private JButton btnSignUp;
    private JButton btnLogin;

    public SignUp() {
        add(panelSignup);
        setVisible(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        btnLogin.addActionListener(e -> {
            Login loginGUI = new Login();
            loginGUI.setVisible(true);
            setVisible(false);
        });
        btnSignUp.addActionListener(e -> {
            String name = txtuserName.getText();
            String email = txtEmail.getText();
            String pass = String.valueOf(txtPass.getPassword());


            Users user = new Users(name, email, pass);


            boolean result1 = usersDAO.addUser(user);
            boolean result2 = usersDAO.authenticateUser(name, pass);

            if(!result1) {
                ConfirmDialog.dialog("Lütfen bilgileri eksiksiz giriniz", 2);
            } else {
                setVisible(false);
                UserHome userHome = new UserHome();
                userHome.setVisible(true);
            }
        });
    }
}
