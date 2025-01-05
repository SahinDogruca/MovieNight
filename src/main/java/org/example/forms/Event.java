package org.example.forms;

import org.example.Helpers.ConfirmDialog;
import org.example.Helpers.UserSession;
import org.example.Models.*;

import javax.swing.*;

import static org.example.Helpers.LoadersApi.*;

public class Event extends JFrame {
    private JPanel panelEvent;
    private JLabel lblOrganizerName;
    private JLabel lblOrganizerEmail;
    private JLabel lblEventName;
    private JLabel lblEventDate;
    private JTable tableFilms;
    private JTable tableSuggestions;
    private JTable tableGuests;
    private JButton btnAddSuggestion;
    private JButton btnDeleteSuggestion;
    private JButton btnExit;



    public Event(Events event, Users organizer) {
        add(panelEvent);
        setTitle("Event");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setVisible(false);




        lblOrganizerName.setText(organizer.getUserName());
        lblOrganizerEmail.setText(organizer.getEmail());
        lblEventName.setText(event.getEventName());
        lblEventDate.setText(event.getEventDate().toString());


        loadMovies(tableFilms);
        loadGuests(event, tableGuests);
        loadSuggestions(event, tableSuggestions);



        btnAddSuggestion.addActionListener(e -> {
            int selectedMovie = Integer.parseInt(tableFilms.getModel().getValueAt(tableFilms.getSelectedRow(), 0).toString());
            System.out.println("selected Movie " + selectedMovie);


            suggestionDAO.addSuggestion(selectedMovie, UserSession.getUserID(), event.getEventID());



            loadSuggestions(event, tableSuggestions);
        });
        btnDeleteSuggestion.addActionListener(e -> {
            int selectedSuggestion = Integer.parseInt(tableSuggestions.getModel().getValueAt(tableSuggestions.getSelectedRow(), 0).toString());
            String userName = tableSuggestions.getModel().getValueAt(tableSuggestions.getSelectedRow(), 2).toString();

            if(UserSession.getUserName().equals(userName)) {
                int out = ConfirmDialog.dialog("Suggestion silmek istediğinize emin misiniz ? ", 1);

                if(out == 0) {
                    suggestionDAO.deleteSuggestion(selectedSuggestion);

                    loadSuggestions(event, tableSuggestions);
                }
            } else {
                ConfirmDialog.dialog("Sadece kendi önerinizi Silebilirsiniz !", 2);
            }


        });
        btnExit.addActionListener(e -> {
            setVisible(false);
            UserHome userHome = new UserHome();
            userHome.setVisible(true);
        });
    }




}
