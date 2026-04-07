package javatutorial;

import java.sql.*;

public class CConnection {
    public Connection cn;

    private String url = "jdbc:mysql://localhost:3306/karyawan_db";
    private String user = "karyawan_user";  // Ganti dengan username MySQL Anda
    private String password = "password123"; // Ganti dengan password MySQL Anda

    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    public void openConnection() {
    try {
        Class.forName(JDBC_DRIVER);
        cn = DriverManager.getConnection(url, user, password);
        System.out.println("✅ Database Connected!");
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}

    public void closeConnection() {
        try {
            if (cn != null && !cn.isClosed()) cn.close();
        } catch (Exception ex) {}
    }
}