package br.fatecsp.es3.domino.ws.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MSSQLConnectionFactory {
	
	
	public static Connection getConnection() throws ClassNotFoundException {
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		
        try {
            return DriverManager.getConnection(
                    "jdbc:sqlserver://den1.mssql1.gear.host;DatabaseName=dominoeng3", "dominoeng3", "Sg68Vox_O0a?");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
