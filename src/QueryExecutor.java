import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Alexander on 05.08.2015.
 */
public class QueryExecutor extends HttpServlet {

    public static final String URL = "jdbc:oracle:thin:@//localhost:1521/xe";
    public static final String username = "Big";
    public static final String password = "Big";

   @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        registerDatabaseDriver();       // Регистрируем драйвер

        String[] dataBaseContent = readFromDatabase();      // Массив записей из базы данных

        String inputString = req.getParameter("input");     // Получаем введённую строку
        String fromJavascript = req.getParameter("fromJavascript");

        if (inputString == null) {

        }
        else {
            String[] matches = searchInDataBaseContent(inputString, dataBaseContent);    // Ищем введённую строку в базе данных

            resp.setContentType("text/html; charset=UTF-8");
            PrintWriter pw = resp.getWriter();

            printResultsInWebPage(matches, pw);
        }

    }

    public static String[] readFromDatabase() {
        String queryToDataBase = "select filmname from films";
        List<String> stringList = new ArrayList<>();

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, username, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(queryToDataBase);

            int i = 0;
            while (resultSet.next()) {
                stringList.add(resultSet.getString("filmname"));
            }

        } catch (SQLException e) {
            System.err.println("Error getting results from DataBase");
        }
        finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Error closing the connection");
                }
            }
        }

        String[] dataBaseContent = new String[stringList.size()];
        for (int i = 0; i < dataBaseContent.length; ++i)
            dataBaseContent[i] = stringList.get(i);

        return dataBaseContent;
    }

    public static String[] searchInDataBaseContent(String inputString, String[] dataBaseContent) {
        String firstOption = "^" + inputString + ".*";
        String secondOption = ".*\\s" + inputString + ".*";
        List<String> matchingStringsList = new ArrayList<>();

        for (int i = 0; i < dataBaseContent.length; ++i) {
            if (matches(firstOption.toLowerCase(), dataBaseContent[i].toLowerCase()) || matches(secondOption.toLowerCase(), dataBaseContent[i].toLowerCase())) {
                matchingStringsList.add(dataBaseContent[i]);
            }
        }

        String[] matchingStrings = new String[matchingStringsList.size()];
        for (int i = 0; i < matchingStrings.length; ++i)
            matchingStrings[i] = matchingStringsList.get(i);

        return matchingStrings;
    }

    public static void printResultsInWebPage(String[] matches, PrintWriter pw) {
        if (matches.length == 0)
            pw.println("No match");
        else {

            for (int i = 0; i < matches.length; ++i) {
                pw.println("<tr><td>" + matches[i] + "</td></tr>");
            }
        }
    }

    public static boolean matches(String inputString, String stringInDataBase) {
        Pattern p = Pattern.compile(inputString);
        Matcher m = p.matcher(stringInDataBase);

        return m.matches();
    }



//------------------ Регистрация драйвера для базы данных -------------------------------------
    public static void registerDatabaseDriver() {
        String driver = "oracle.jdbc.driver.OracleDriver";
        Locale locale = Locale.getDefault();
        Locale.setDefault(Locale.ENGLISH);

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.err.println("Cannot register a driver");
        }
    }
//---------------------------------------------------------------------------------------------
}
