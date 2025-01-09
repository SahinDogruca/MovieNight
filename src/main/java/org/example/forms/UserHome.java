package org.example.forms;

import org.example.Helpers.ConfirmDialog;
import org.example.Helpers.UserSession;
import org.example.Models.Events;
import org.example.Models.Invitation;
import org.example.Models.Users;
import org.postgresql.util.PSQLException;

import javax.swing.*;
import java.sql.Date;

import static org.example.Helpers.LoadersApi.*;

public class UserHome extends JFrame {
    private JPanel panelUserHome;
    private JTabbedPane tabbedPane1;
    private JPanel Profil;
    private JButton btnSubmit;
    private JPasswordField txtNewPass;
    private JPasswordField txtCurrPass;
    private JTextField txtNewEmail;
    private JTextField txtName;
    private JPanel panelMovies;
    private JTable tableMovies;
    private JTextField txtRating;
    private JButton btnVote;
    private JTable tableInvitations;
    private JButton kabulEtButton;
    private JButton reddetButton;
    private JTable tableEvents;
    private JTextField txtEventName;
    private JTextField txtEventDate;
    private JButton btnAddEvent;
    private JButton btnDeleteEvent;
    private JTextField txtGuestName;
    private JButton btnSendInvitation;
    private JButton btnEventDetail;


    public UserHome() {
        add(panelUserHome);
        setTitle("User Home");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setVisible(false);

        loadMovies(tableMovies);
        loadEvents(tableEvents);

        tabbedPane1.addChangeListener(e -> {
            int selectedIndex = tabbedPane1.getSelectedIndex();
            String selectedLabel = tabbedPane1.getTitleAt(selectedIndex);
            if(selectedLabel.equals("Davetiyeler")) {
                loadInvitations(tableInvitations);
            }
        });

        tabbedPane1.addChangeListener(e -> {
            int selectedIndex = tabbedPane1.getSelectedIndex();
            String selectedLabel = tabbedPane1.getTitleAt(selectedIndex);
            if(selectedLabel.equals("Profil")) {
                txtName.setText(UserSession.getUserName());
            }
        });

        tabbedPane1.addChangeListener(e -> {
            int selectedIndex = tabbedPane1.getSelectedIndex();
            String selectedLabel = tabbedPane1.getTitleAt(selectedIndex);
            if(selectedLabel.equals("Eventler")) {
                loadEvents(tableEvents);
            }
        });

        tabbedPane1.addChangeListener(e -> {
            int selectedIndex = tabbedPane1.getSelectedIndex();
            String selectedLabel = tabbedPane1.getTitleAt(selectedIndex);
            if(selectedLabel.equals("Çıkış Yap")) {
                UserSession.logout();
                setVisible(false);
                Login login = new Login();
                login.setVisible(true);
            }
        });
        btnVote.addActionListener(e -> {


            try {
                int rating = 10;
                try {
                    rating = Integer.parseInt(txtRating.getText());
                } catch (Exception ex) {
                    ConfirmDialog.dialog( "Oy puanı boş olamaz", 2);
                }

                String selectedTitle = tableMovies.getModel().getValueAt(tableMovies.getSelectedRow(), 1).toString();

                voteDAO.addVote(selectedTitle, UserSession.getUserName(), rating);
                loadMovies(tableMovies);

            } catch (IndexOutOfBoundsException e1) {
                ConfirmDialog.dialog( "Lütfen geçerli bir film seçiniz!", 2);
            } catch(IllegalArgumentException e2) {
                ConfirmDialog.dialog( e2.getMessage(), 2);
            } catch (RuntimeException e3) {
                ConfirmDialog.dialog( " Aynı filme 2 kere vote verilemez.", 2);
            }


            txtRating.setText("");


        });
        btnSubmit.addActionListener(e -> {
            String name = txtName.getText();

            String CurrPass = String.valueOf(txtCurrPass.getPassword());
            String NewPass = String.valueOf(txtNewPass.getPassword());


            boolean result = usersDAO.updateUserPassword(name, CurrPass, NewPass);

            if(!result) {
                ConfirmDialog.dialog( "Lütfen bilgilerinizi kontrol ediniz", 2);
            }


            txtNewPass.setText("");
            txtCurrPass.setText("");
        });
        kabulEtButton.addActionListener(e -> {

            try {
                int invitationId = Integer.parseInt(tableInvitations.getModel().getValueAt(tableInvitations.getSelectedRow(), 0).toString());

                invitationDAO.updateInvitationStatus(invitationId, Invitation.Status.ACCEPTED);

                loadInvitations(tableInvitations);

            } catch (IndexOutOfBoundsException e1) {
                ConfirmDialog.dialog( "Lütfen geçerli bir davetiye seçiniz!", 2);
            }

        });

        reddetButton.addActionListener(e -> {

            try {
                int invitationId = Integer.parseInt(tableInvitations.getModel().getValueAt(tableInvitations.getSelectedRow(), 0).toString());

                invitationDAO.updateInvitationStatus(invitationId, Invitation.Status.DECLINED);

                loadInvitations(tableInvitations);
            } catch (IndexOutOfBoundsException e1) {
                ConfirmDialog.dialog( "Lütfen geçerli bir davetiye seçiniz!", 2);
            }

        });
        btnAddEvent.addActionListener(e -> {
            String eventName = txtEventName.getText();
            String eventDate = txtEventDate.getText();

            try {
                Events event = new Events(0, UserSession.getUserID(), eventName, Date.valueOf(eventDate).toLocalDate());
                eventsDAO.addEvent(event);
                loadEvents(tableEvents);

            } catch (IllegalArgumentException e1) {
                ConfirmDialog.dialog( "Lütfen bilgileri eksiksiz doldurunuz!", 2);
            }

        });
        btnDeleteEvent.addActionListener(e -> {

            try {
                int selectedIndex = Integer.parseInt(tableEvents.getModel().getValueAt(tableEvents.getSelectedRow(), 0).toString());

                if(eventsDAO.getEventByEventId(selectedIndex).getUserID() == UserSession.getUserID()) {
                    eventsDAO.deleteEvent(selectedIndex);
                    loadEvents(tableEvents);
                } else {
                    ConfirmDialog.dialog( "Sadece kendi düzenlediğiniz etkinliği silebilirsiniz !", 2);
                }

            } catch (IndexOutOfBoundsException e1) {
                ConfirmDialog.dialog( "Lütfen geçerli bir etkinlik seçiniz", 2);
            }

        });
        btnSendInvitation.addActionListener(e -> {
            String guestName = txtGuestName.getText();

            try {
                int selectedIndex = Integer.parseInt(tableEvents.getModel().getValueAt(tableEvents.getSelectedRow(), 0).toString());

                boolean result = invitationDAO.addInvitation(selectedIndex, guestName);

                if(!result) {
                    ConfirmDialog.dialog( "Kullanıcı kabul edilmedi ! ", 2);
                }
            } catch (IndexOutOfBoundsException e1) {
                ConfirmDialog.dialog( "Bir Event Seçmelisiniz !", 2);
            }





        });
        btnEventDetail.addActionListener(e -> {
            try {
                int selectedIndex = Integer.parseInt(tableEvents.getModel().getValueAt(tableEvents.getSelectedRow(), 0).toString());

                Events event = eventsDAO.getEventByEventId(selectedIndex);
                Users user = usersDAO.getUserById(event.getUserID());


                Event eventGUI = new Event(event, user);
                setVisible(false);
                eventGUI.setVisible(true);

            } catch (IndexOutOfBoundsException e1) {
                ConfirmDialog.dialog( "Lütfen geçerli bir etkinlik seçiniz!", 2);
            }



        });
    }


}
