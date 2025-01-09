package org.example.Helpers;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class DatabaseConfig {
    static Dotenv dotenv = Dotenv.load();



    private static final String URL = dotenv.get("DB_URL");
    private static final String NAME = dotenv.get("DB_NAME");
    private static final String USER = dotenv.get("DB_USER");
    private static final String PASSWORD = dotenv.get("DB_PASSWORD");



    public static Connection connect() {
        try {

            Connection connection = DriverManager.getConnection(URL + NAME, USER, PASSWORD);

            System.out.println("Veritabanına başarıyla bağlanıldı!");
            return connection;
        } catch (SQLException e) {
            System.out.println("Veritabanına bağlanılamadı: " + e.getMessage());
            throw new RuntimeException("Veritabanı bağlantısı başarısız.", e);
        }
    }

    public static void insertTables() {
        try(Connection connection = DriverManager.getConnection(URL + NAME, USER, PASSWORD)) {
            executeSqlScript(connection, "createTables.sql");
            executeSqlScript(connection, "fillTables.sql");
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void checkDatabase() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            if (!doesDatabaseExist(conn, NAME)) {

                createDatabase(conn, NAME);
                insertTables();


            } else {
                System.out.println("Database already exists.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean doesDatabaseExist(Connection conn, String dbName) throws SQLException {
        String query = "SELECT 1 FROM pg_database WHERE datname = '" + dbName + "'";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            return rs.next();  // If a record is returned, the database exists
        }
    }

    // Function to create the database
    private static void createDatabase(Connection conn, String dbName) throws SQLException {
        String createDbQuery = "CREATE DATABASE " + dbName;
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createDbQuery);
        }
    }

    private static void executeSqlScript(Connection conn, String sqlScriptFile) throws SQLException, IOException {
        // Read the SQL script from the file
        try (BufferedReader reader = new BufferedReader(new FileReader(sqlScriptFile));
             Statement stmt = conn.createStatement()) {
            String line;
            StringBuilder sqlQuery = new StringBuilder();

            // Read the SQL script line by line
            while ((line = reader.readLine()) != null) {
                sqlQuery.append(line).append("\n");
            }

            // Execute the SQL script
            stmt.execute(sqlQuery.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
