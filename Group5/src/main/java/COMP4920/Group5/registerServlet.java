package COMP4920.Group5;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.SQLiteUserSelect;
import user.User;

public class registerServlet extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String username = null;
		String password = null;
		String repeatPassword = null;
		String name = null;
		
		boolean registerSuccess = true;
		
		try {
			username = req.getParameter("username");
			password = req.getParameter("password");
			repeatPassword = req.getParameter("repeatPassword");
			name = req.getParameter("name");
		} catch (Exception e) {
			registerSuccess = false;
			req.setAttribute("message", "Please provide complete details");
		}
		System.out.println(username+" "+password+" "+repeatPassword+" "+name);
		
		if(registerSuccess)
			if(username==null || password == null || repeatPassword == null || name == null) {
				registerSuccess = false;
				req.setAttribute("message", "Please provide complete details");
			}
		
		if(registerSuccess)
			if(username.equals("") || password.equals("") || repeatPassword.equals("") || name.equals("")) {
				registerSuccess = false;
				req.setAttribute("message", "Please provide complete details");
			}
		
		if(registerSuccess)
			if(!password.equals(repeatPassword)) {
				registerSuccess = false;
				req.setAttribute("message", "Password not match!");
			}
		ArrayList<User> userList = SQLiteUserSelect.selectAllUser();
		
		if(registerSuccess) {
			for (User u : userList) {
				if (u.getUsername().equals(username)) {
					registerSuccess = false;
					req.setAttribute("message", "Username has been used!");
				}
			}	
		}		
		if(registerSuccess) {
			User newUser = new User(username, password, name, "USER");
			newUser.addToDB();
			String successMsg = "Register success, please login.";
			req.setAttribute("message",successMsg);
			req.getRequestDispatcher("index.jsp").forward(req, resp);
		} else {
			req.getRequestDispatcher("register.jsp").forward(req, resp);
		}
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
