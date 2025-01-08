package org.example.Helpers;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    static Dotenv dotenv = Dotenv.load();



    private static final String URL = dotenv.get("DB_URL");
    private static final String USER = dotenv.get("DB_USER");
    private static final String PASSWORD = dotenv.get("DB_PASSWORD");

    public static Connection connect() {
        try {
            // Bağlantı
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Veritabanına başarıyla bağlanıldı!");
            return connection;
        } catch (SQLException e) {
            System.out.println("Veritabanına bağlanılamadı: " + e.getMessage());
            throw new RuntimeException("Veritabanı bağlantısı başarısız.", e);
        }
    }
}
