package org.example.forms;


import org.example.Helpers.ConfirmDialog;
import org.example.Helpers.UserSession;

import javax.swing.*;

import static org.example.Helpers.LoadersApi.adminDAO;
import static org.example.Helpers.LoadersApi.usersDAO;

public class Login extends JFrame {
    private JTextField txtUserName;
    private JPasswordField txtUserPassword;
    private JButton btnLogin;
    private JButton btnSignUp;
    private JPanel panel1;
    private JRadioButton adminRadioButton;
    private JRadioButton userRadioButton;

    public Login() {
        add(panel1);
        setSize(800, 600);
        setTitle("Movie Night");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        btnLogin.addActionListener(e -> {
            String name = txtUserName.getText();
            String pass = String.valueOf(txtUserPassword.getPassword());
            boolean result;
            if(adminRadioButton.isSelected()) {
                result = adminDAO.authenticateUser(name, pass);
            } else {
                result = usersDAO.authenticateUser(name, pass);
            }

            if(!result) {
                ConfirmDialog.dialog( "Lütfen bilgileri doğru ve eksiksiz girdiğinizden emin olunuz !", 2);
            } else {

                if(UserSession.isLoggedIn() && UserSession.isAdmin()) {
                    setVisible(false);
                    AdminHome adminHome = new AdminHome();
                    adminHome.setVisible(true);
                } else if(UserSession.isLoggedIn()) {
                    setVisible(false);
                    UserHome userHome = new UserHome();
                    userHome.setVisible(true);
                }
            }


        });
        btnSignUp.addActionListener(e -> {
            setVisible(false);
            SignUp signUp = new SignUp();
            signUp.setVisible(true);
        });
    }
}
