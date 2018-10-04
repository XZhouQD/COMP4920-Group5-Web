package COMP4920.Group5;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.*;
import user.User;

public class loginServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		System.out.println(username+" "+password);
		boolean loginSuccess = false;		
		ArrayList<User> userList = SQLiteUserSelect.selectAllUser();
		User sessionUser = null;
		for (User u : userList) {
			if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
				loginSuccess = true;
				sessionUser = u;
			}
		}
		if(loginSuccess) {
			Date curDate = new Date();
			SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss a zzz E dd/MM/yyyy");
			String curTime = ft.format(curDate);
			req.setAttribute("time", curTime);
			req.setAttribute("name", sessionUser.getName());
			req.getRequestDispatcher("LoginSuccess.jsp").forward(req, resp);
		} else {
			String failureMsg = "Login failure, please check provided details.";
			req.setAttribute("message", failureMsg);
			req.getRequestDispatcher("index.jsp").forward(req, resp);
		}
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
