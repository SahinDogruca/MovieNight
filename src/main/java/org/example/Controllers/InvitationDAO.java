package org.example.Controllers;

import org.example.Helpers.DatabaseConfig;
import org.example.Models.Events;
import org.example.Models.Invitation;

import java.sql.*;
import java.util.ArrayList;

import static org.example.Helpers.LoadersApi.eventsDAO;
import static org.example.Helpers.LoadersApi.usersDAO;

public class InvitationDAO {

    public boolean addInvitation(int eventID,int userSessionId, String username) {
        if (isNullOrEmpty(username, "Hata: Kullanıcı adı boş bırakılamaz.")) {
            return false;
        }

        // Kullanıcı ID'sini kullanıcı adı üzerinden alıyoruz
        int userID = usersDAO.getUserIDByUsername(username);
        if (userID == -1) {
            System.out.println("Hata: Davet gönderilecek kullanıcı bulunamadı.");
            return false;
        }


        if (eventID == -1) {
            System.out.println("Hata: Etkinlik bulunamadı.");
            return false;
        }

        try (Connection connection = DatabaseConfig.connect()) {

            String sql = "{? = CALL send_invitation_with_cursor(?,?,?,?)}";

            try(CallableStatement cstmt = connection.prepareCall(sql)) {
                cstmt.registerOutParameter(1, Types.VARCHAR);

                cstmt.setInt(2, eventID);
                cstmt.setInt(3, userSessionId);
                cstmt.setInt(4, userID);
                cstmt.setString(5, Invitation.Status.PENDING.toString());

                cstmt.execute();

                String result = cstmt.getString(1);


                if(result.equals("Sadece etkinlik sahibi davetiye gönderebilir.")) {
                    throw new IllegalArgumentException(result);
                } if(result.equals("Davetiye başarıyla gönderildi.")) {
                    return true;
                }


            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    public ArrayList<Events> getAllAcceptedInvitations(int user_id) {
        ArrayList<Events> acceptedInvitations = new ArrayList<>();

        String sql = "SELECT event_id FROM invitations WHERE user_id = ? and status = 'ACCEPTED'";

        try(Connection connection = DatabaseConfig.connect();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, user_id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Events event = eventsDAO.getEventByEventId(resultSet.getInt("event_id"));

                acceptedInvitations.add(event);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return acceptedInvitations;
    }

    public ArrayList<Invitation> getAllPendingInvitations(int user_id) {
        String sql = "SELECT * FROM invitations WHERE user_id = ? and status = 'PENDING'";
        ArrayList<Invitation> invitations = new ArrayList<>();

        try(Connection connection = DatabaseConfig.connect();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, user_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {

                    Invitation invitation = new Invitation(0, resultSet.getInt("event_id"), user_id, Invitation.Status.PENDING);
                    invitation.setInvitationID(resultSet.getInt("invitation_id"));

                    invitations.add(invitation);

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return invitations;
    }

    public ArrayList<Invitation> getInvitationsByEvent(int event_id) {
        String sql = "SELECT * FROM invitations WHERE event_id = ? and status = 'ACCEPTED'";
        ArrayList<Invitation> invitations = new ArrayList<>();

        try(Connection connection = DatabaseConfig.connect();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, event_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {


                    Invitation invitation = new Invitation(resultSet.getInt("invitation_id"), event_id, resultSet.getInt("user_id"), Invitation.Status.ACCEPTED);

                    invitations.add(invitation);

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return invitations;
    }

    public void updateInvitationStatus(int invitationID, Invitation.Status status) {
        String sql = "UPDATE invitations SET status = ? WHERE invitation_id = ?";

        try(Connection connection = DatabaseConfig.connect();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, status.toString());
            statement.setInt(2, invitationID);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Davet başarıyla gönderildi!");
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    // Yardımcı metodlar
    private boolean isNullOrEmpty(String value, String errorMessage) {
        if (value == null || value.trim().isEmpty()) {
            System.out.println(errorMessage);
            return true;
        }
        return false;
    }

    private void handleSQLException(SQLException e, String defaultMessage) {
        System.out.println(defaultMessage + " " + e.getMessage());
    }


}
