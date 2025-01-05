package org.example;

import org.example.forms.Login;


import javax.swing.*;


// TODO suggestion eklerken movie_id ve user_id aynı olmamalı
// TODO event sahibi event'e kendini davet edememeli
// TODO add admin ve delete admin implemente et
// TODO movie bilgilerini değiştir genre falan gibi bir durum eklenebilir


public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                Login loginGUI = new Login();

                loginGUI.setVisible(true);
            }
        });
    }



}
