package org.example.Controllers;

import org.example.Helpers.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.example.Helpers.LoadersApi.moviesDAO;
import static org.example.Helpers.LoadersApi.usersDAO;

public class VoteDAO {


    public boolean addVote(String movieName, String userName, int rating)
    {
        isNullOrEmpty(userName, "Hata : kullanıcı adı boş bırakılamaz.");
        isNullOrEmpty(movieName, "Hata : film adı boş bırakılamaz");



        // Film ID'sini kullanıcı adı üzerinden alıyoruz

        int moviesID = moviesDAO.getMovieByTitle(movieName);
        if (moviesID == -1) {
            System.out.println("Hata: Film bulunamadı.");
            return false;
        }


        int usersID = usersDAO.getUserIDByUsername(userName);
        if (usersID == -1) {
            System.out.println("Hata: Etkinlik bulunamadı.");
            return false;
        }

        String sql = "INSERT INTO vote (movie_id, user_id, rating) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConfig.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, moviesID);
            statement.setInt(2, usersID);
            statement.setInt(3, rating);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Vote başarıyla gönderildi!");
                return true;
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }


    private void isNullOrEmpty(String value, String errorMessage) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
