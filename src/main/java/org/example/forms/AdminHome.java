package org.example.forms;


import org.example.Helpers.ConfirmDialog;
import org.example.Helpers.LoadersApi;
import org.example.Helpers.UserSession;
import org.example.Models.Movies;
import org.example.Models.Users;

import javax.swing.*;
import java.sql.Date;

import static org.example.Helpers.LoadersApi.*;

public class AdminHome extends JFrame {
    private JPanel panelAdmin;
    private JTabbedPane tabbedPane1;
    private JPanel panelUsers;
    private JPanel panelProfile;
    private JPanel deneme;
    private JTextField txtName;
    private JTextField txtEmail;
    private JTextField txtPassword;
    private JButton btnSubmit;
    private JTable tableUsers;
    private JButton btnAddUser;
    private JTable tableMovies;
    private JPanel panelMovies;
    private JPanel panelLogout;
    private JPasswordField txtNewPass;
    private JPasswordField txtCurrPass;
    private JTextField txtNewEmail;
    private JTextField txtMovieTitle;
    private JTextField txtMovieGenre;
    private JTextField txtMovieRd;
    private JButton btnAddMovie;
    private JButton btnDeleteMovie;
    private JButton btnDeleteUser;
    private JTextField txtUserName;
    private JTextField txtUserEmail;
    private JPasswordField txtUserPass;


    public AdminHome() {
        add(panelAdmin);
        setTitle("Admin Home");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setVisible(false);

        LoadersApi.loadUsers(tableUsers);


        tabbedPane1.addChangeListener(e -> {
            int selectedIndex = tabbedPane1.getSelectedIndex();
            String selectedLabel = tabbedPane1.getTitleAt(selectedIndex);
            if(selectedLabel.equals("Filmler")) { // profile
                LoadersApi.loadMovies(tableMovies);
            }
        });



        tabbedPane1.addChangeListener(e -> {
            int selectedIndex = tabbedPane1.getSelectedIndex();
            String selectedLabel = tabbedPane1.getTitleAt(selectedIndex);
            if(selectedLabel.equals("Profil")) { // profile
                txtName.setText(UserSession.getUserName());
            }
        });



        tabbedPane1.addChangeListener(e -> {
            int selectedIndex = tabbedPane1.getSelectedIndex();
            String selectedLabel = tabbedPane1.getTitleAt(selectedIndex);
            if(selectedLabel.equals("Çıkış Yap")) {
                UserSession.logout();
                setVisible(false);
                Login loginGUI = new Login();
                loginGUI.setVisible(true);
            }
        });
        btnSubmit.addActionListener(e -> {
            String name = txtName.getText();
            String newEmail = txtNewEmail.getText();
            String CurrPass = String.valueOf(txtCurrPass.getPassword());
            String NewPass = String.valueOf(txtNewPass.getPassword());


            boolean resultEmail = adminDAO.updateAdminEmail(name, CurrPass, newEmail);
            boolean resultPass = adminDAO.updateAdminPassword(name, CurrPass, NewPass);

            if(!resultEmail || !resultPass) {
                ConfirmDialog.dialog("Bilgileri Kontrol ediniz!", 2);
            }

            txtNewEmail.setText("");
            txtNewPass.setText("");
            txtCurrPass.setText("");
        });
        btnAddUser.addActionListener(e -> {
            String name = txtUserName.getText();
            String email = txtUserEmail.getText();
            String password = String.valueOf(txtUserPass.getPassword());



            try {
                Users user = new Users(name, email, password);
                boolean result = usersDAO.addUser(user);

                if(!result) {
                    ConfirmDialog.dialog( "Bilgileri Eksiksiz giriniz", 2);
                } else {
                    LoadersApi.loadUsers(tableUsers);
                }


            } catch (IllegalArgumentException e1) {
                ConfirmDialog.dialog( e1.getMessage(), 2);
            }



            txtUserName.setText("");
            txtUserEmail.setText("");
            txtUserPass.setText("");


        });
        btnAddMovie.addActionListener(e -> {
            String title = txtMovieTitle.getText();
            String genre = txtMovieGenre.getText();
            String releaseDateInput = txtMovieRd.getText();
            Date releaseDate = null;
            try {
                releaseDate = Date.valueOf(releaseDateInput);
            } catch (Exception e1) {
                ConfirmDialog.dialog( "Bilgileri boş bırakmayınız", 2);
            }


            try {



                Movies movie = new Movies(0, title, genre, releaseDate.toLocalDate(), 5);
                moviesDAO.addMovie(movie);
                LoadersApi.loadMovies(tableMovies);


            }catch (IllegalArgumentException e1) {
                ConfirmDialog.dialog( e1.getMessage(), 2);
            } catch(NullPointerException e1) {
                ConfirmDialog.dialog( "tarih formatı YYYY-MM-DD", 2);
            }


            txtMovieTitle.setText("");
            txtMovieGenre.setText("");
            txtMovieRd.setText("");


        });
        btnDeleteMovie.addActionListener(e -> {

            try {

                String selectedTitle = tableMovies.getModel().getValueAt(tableMovies.getSelectedRow(), 1).toString();
                moviesDAO.deleteMovie(selectedTitle);
                LoadersApi.loadMovies(tableMovies);


            } catch (IndexOutOfBoundsException e1) {
                ConfirmDialog.dialog( "Lütfen geçerli bir film seçiniz!", 2);
            }



        });

        btnDeleteUser.addActionListener(e -> {

            try {
                String selectedName = tableUsers.getModel().getValueAt(tableUsers.getSelectedRow(), 1).toString();
                usersDAO.deleteUserByName(selectedName);
                LoadersApi.loadUsers(tableUsers);
            } catch(IndexOutOfBoundsException e1) {
                ConfirmDialog.dialog("Lütfen geçerli bir kullanıcı seçiniz ! ", 2);
            }

        });
    }





}
