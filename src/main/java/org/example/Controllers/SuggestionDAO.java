package org.example.Controllers;

import org.example.Helpers.DatabaseConfig;
import org.example.Models.Suggestion;

import java.sql.*;
import java.util.ArrayList;

public class SuggestionDAO {

    public void addSuggestion(int movie_id, int user_id, int event_id) {
        String sql = "INSERT INTO suggestions(event_id, user_id, movie_id) VALUES (?,?,?)";

        try(Connection connection = DatabaseConfig.connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, event_id);
            preparedStatement.setInt(2, user_id);
            preparedStatement.setInt(3, movie_id);

            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected > 0) {
                System.out.println("Added suggestion");
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Suggestion> getSuggestionsByEvent(int event_id) {
        String sql = "SELECT * FROM suggestions WHERE event_id = ?";
        ArrayList<Suggestion> suggestions = new ArrayList<>();
        Suggestion suggestion;

        try(Connection connection = DatabaseConfig.connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, event_id);


            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                while(resultSet.next()) {
                    suggestion = new Suggestion(resultSet.getInt("suggestion_id"),
                                                event_id,
                                                resultSet.getInt("user_id"),
                                                resultSet.getInt("movie_id")
                                                );

                    suggestions.add(suggestion);
                }
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return suggestions;
    }

    public void deleteSuggestion(int suggestion_id) {
        String sql = "DELETE FROM suggestions WHERE suggestion_id = ?";

        try(Connection connection = DatabaseConfig.connect();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, suggestion_id);

            int rowsAffected = statement.executeUpdate();
            if(rowsAffected > 0) {
                System.out.println("Deleted suggestion");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
