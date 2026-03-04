package com.expense.app;

import java.sql.Connection;
import java.sql.DriverManager;


public class DButil {
	private static final String URL=  "jdbc:mysql://localhost:3306/expense_tracker?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";       // your MySQL username
    private static final String PASSWORD = "Sitara@3459";

    public static Connection getConnection() {
        try {
             Class.forName("com.mysql.cj.jdbc.Driver");        
             
             return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            System.out.println("❌ Database connection failed");
            e.printStackTrace();
            return null;
        }
    }

}
