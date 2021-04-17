package app.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtils {
    // jdbc:mysql://hostname:port/dbname
//
//    private static final String hostName ="localhost";// "192.168.1.254";
//    private static final String dbName = "testdb";//"chamcongdb";
//    private static final String userName = "root";
//    private static final String password = "Tutu561997";


    private static final String hostName = "192.168.1.254";
    private static final String dbName = "simdb";
    private static final String userName = "dung";
    private static final String password = "Tutu561997";


    private static final String connectionURL = "jdbc:mysql://" + hostName + ":3306/" + dbName;

    public static Connection openConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(connectionURL, userName, password);
        } catch (SQLException | ClassNotFoundException e){
            System.out.println("Loi ket noi SQL");
            e.printStackTrace();
        }
        return null;
    }
}
