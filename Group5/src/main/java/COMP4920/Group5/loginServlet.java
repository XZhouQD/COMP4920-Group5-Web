package COMP4920.Group5;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class loginServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		System.out.println(username+" "+password);
		
		Date curDate = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss a zzz E dd/MM/yyyy");
		String curTime = ft.format(curDate);
		req.setAttribute("time", curTime);
		req.setAttribute("name", "John Doe");
		req.getRequestDispatcher("LoginSuccess.jsp").forward(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
