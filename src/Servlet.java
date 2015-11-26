import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Alexander on 12.08.2015.
 */
public class Servlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter pw = resp.getWriter();
        HttpSession hs = req.getSession();
        String[] output = null;

        String input = req.getParameter("input");
        if (input.matches("\\d")) {
            String queryToDB = "select * from books where shelf = '" + input + "'";
            output = DataBaseHandler.findInDataBase(queryToDB);
        }
        else {
            String queryToDB = "select * from books where bookauthor = '" + input + "'";
            output = DataBaseHandler.findInDataBase(queryToDB);
        }

        hs.setAttribute("output", output);
        resp.sendRedirect("/results.jsp");
    }
}
