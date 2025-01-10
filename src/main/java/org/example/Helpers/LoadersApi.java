package org.example.Helpers;

import org.example.Controllers.*;
import org.example.Models.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoadersApi {
    public static final SuggestionDAO suggestionDAO = new SuggestionDAO();
    public static final MoviesDAO moviesDAO = new MoviesDAO();
    public static final UsersDAO usersDAO = new UsersDAO();
    public static final InvitationDAO invitationDAO = new InvitationDAO();
    public static final EventsDAO eventsDAO = new EventsDAO();
    public static final VoteDAO voteDAO = new VoteDAO();
    public static final AdminDAO adminDAO = new AdminDAO();

    public static void loadMovies(JTable tableMovies) {
        ArrayList<Movies> movies;

        movies = moviesDAO.getAllMovies();

        String[] columns = { "id", "title", "genre", "release_date", "rating"};
        DefaultTableModel model = new DefaultTableModel(null, columns);
        for(Movies movie : movies) {
            model.addRow(new Object[] {movie.getMovieID(), movie.getTitle(), movie.getGenre(), movie.getReleaseDate(), movie.getRating() });
        }
        tableMovies.setModel(model);
    }

    public static void loadUsers(JTable tableUsers) {
        ArrayList<Users> users;
        try {
            users = usersDAO.getAllUsers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String[] columns = { "id", "username", "email", "password"};
        DefaultTableModel model = new DefaultTableModel(null, columns);

        for(Users user : users) {
            model.addRow(new Object[] { user.getUserID(), user.getUserName(), user.getEmail(), user.getPassword() });
        }
        tableUsers.setModel(model);
    }


    public static void loadInvitations(JTable tableInvitations) {

        ArrayList<Invitation> invitations;

        invitations = invitationDAO.getAllPendingInvitations(UserSession.getUserID());

        String[] columns = { "id", "event_id", "event_name", "organizer_name", "organizer_email", "event_date"};


        DefaultTableModel model = new DefaultTableModel(null, columns);

        for(Invitation invitation : invitations) {
            Events event = eventsDAO.getEventByEventId(invitation.getEventID());
            Users user = usersDAO.getUserById(event.getUserID());
            model.addRow(new Object[]{invitation.getInvitationID(), event.getEventID(), event.getEventName(),user.getUserName(), user.getEmail(), event.getEventDate()});
        }

        tableInvitations.setModel(model);

    }

    public static void loadEvents(JTable tableEvents) {

        ArrayList<Events> events;
        ArrayList<Events> acceptedInvitations;

        events = eventsDAO.getEventsByUserId(UserSession.getUserID());

        String[] columns = {"id", "event_name", "event_date" ,"organizer_name", "organizer_email"};

        DefaultTableModel model = new DefaultTableModel(null, columns);
        for(Events event : events) {
            Users user = usersDAO.getUserById(event.getUserID());
            String username = UserSession.getUserName().equals(user.getUserName()) ? user.getUserName() + " (siz)" : user.getUserName();
            model.addRow(new Object[]{event.getEventID(), event.getEventName(), event.getEventDate(), username, user.getEmail()});
        }


        tableEvents.setModel(model);
    }

    public static void loadSuggestions(Events event, JTable tableSuggestions) {
        ArrayList<Suggestion> suggestions;

        suggestions = suggestionDAO.getSuggestionsByEvent(event.getEventID());

        String[] columns = {"id", "movie_title", "guest_name"};
        DefaultTableModel model = new DefaultTableModel(null, columns);


        for(Suggestion suggestion : suggestions) {
            String username = usersDAO.getUserById(suggestion.getUserID()).getUserName();
            String movietitle = moviesDAO.getMovieByID(suggestion.getMovieID()).getTitle();

            model.addRow(new Object[]{suggestion.getSuggestionID(), movietitle, username});
        }

        tableSuggestions.setModel(model);
    }

    public static void loadGuests(Events event, JTable tableGuests) {
        ArrayList<Invitation> guests;

        guests = invitationDAO.getInvitationsByEvent(event.getEventID());

        String[] columns = {"id", "guest_id","name", "email"};
        DefaultTableModel model = new DefaultTableModel(null, columns);

        for(Invitation invitation : guests) {
            Users user = usersDAO.getUserById(invitation.getUserID());

            model.addRow(new Object[]{invitation.getInvitationID(), user.getUserID(), user.getUserName(), user.getEmail()});
        }

        tableGuests.setModel(model);
    }
}
