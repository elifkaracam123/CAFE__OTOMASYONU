package com.cafeautomation.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/cafe_automation"; // Veri tabanı adı
    private static final String USER = "root"; // MySQL kullanıcı adı
    private static final String PASSWORD = "admin123"; // MySQL şifresi

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Veritabanına bağlanılamadı.");
        }
    }
}
