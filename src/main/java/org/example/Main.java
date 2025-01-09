package org.example;

import org.example.forms.Login;


import javax.swing.*;

import static org.example.Helpers.DatabaseConfig.checkDatabase;





public class Main {
    public static void main(String[] args) {

        checkDatabase();


        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                Login loginGUI = new Login();

                loginGUI.setVisible(true);
            }
        });
    }



}
