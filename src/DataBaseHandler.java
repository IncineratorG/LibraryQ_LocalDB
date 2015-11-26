import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Alexander on 07.09.2015.
 */
public class DataBaseHandler {
    public static final String URL = "jdbc:oracle:thin:@//localhost:1521/xe";
    public static final String username = "Big";
    public static final String password = "Big";

    public static String[] authors = dbContent("bookauthor");
    public static String[] booknames = dbContent("bookname");
    public static String[] shelfs = dbContent("shelf");




    public static String[] dbContent(String column) {
        String driver = "oracle.jdbc.driver.OracleDriver";
        Locale locale = Locale.getDefault();
        Locale.setDefault(Locale.ENGLISH);

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.err.println("Cannot register a driver");
        }

        List<String> dbContent = new ArrayList<>();

        try(Connection c = DriverManager.getConnection(URL, username, password);
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("select DISTINCT " + column + " from books")) {

            while (rs.next()) {
                dbContent.add(rs.getString(column));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String[] db = new String[dbContent.size()];
        for (int i = 0; i < db.length; ++i)
            db[i] = dbContent.get(i);

        return db;
    }

    public static String[] findInDataBase(String string) {
        String driver = "oracle.jdbc.driver.OracleDriver";
        Locale locale = Locale.getDefault();
        Locale.setDefault(Locale.ENGLISH);

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.err.println("Cannot register a driver");
        }

        List<String> dbContent = new ArrayList<>();
        String query = string;

        try(Connection c = DriverManager.getConnection(URL, username, password);
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(query)) {

            while (rs.next()) {
                dbContent.add(rs.getString("shelf"));
                dbContent.add(rs.getString("bookauthor"));
                dbContent.add(rs.getString("bookname"));
                dbContent.add(rs.getString("deleted"));
                dbContent.add(rs.getString("id_b"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String[] db = new String[dbContent.size()];
        for (int i = 0; i < db.length; ++i)
            db[i] = dbContent.get(i);

        return db;
    }

    public static void executeQuery(String query) {
        String driver = "oracle.jdbc.driver.OracleDriver";
        Locale locale = Locale.getDefault();
        Locale.setDefault(Locale.ENGLISH);

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.err.println("Cannot register a driver");
        }

        try(Connection c = DriverManager.getConnection(URL, username, password);
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(query)) {
               c.setAutoCommit(true);
        } catch (SQLException e) {
            System.err.println("executeQuery() error.");
            e.printStackTrace();
        }
    }

    public static void addNewBook(String bookauthor, String bookname, String shelf, String other) {
        String driver = "oracle.jdbc.driver.OracleDriver";
        Locale locale = Locale.getDefault();
        Locale.setDefault(Locale.ENGLISH);

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.err.println("Cannot register a driver");
        }

        try(Connection c = DriverManager.getConnection(URL, username, password);
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("insert into books (bookauthor, bookname, shelf, deleted) values ('" + bookauthor + "', '" + bookname + "', '" + shelf + "', '" + "0" + "')")) {

            c.setAutoCommit(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
