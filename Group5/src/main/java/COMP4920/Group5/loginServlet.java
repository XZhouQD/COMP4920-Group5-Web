package COMP4920.Group5;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.*;
import food.Food;
import user.User;

public class loginServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		System.out.println(username+" "+password);
		
		//SQLiteNewDatabase.newDatabase();
		
		User sessionUser = null;
		boolean loginSuccess = false;
		
		ArrayList<User> userList = SQLiteUserSelect.selectAllUser();
		
		for (User u : userList) {
			if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
				loginSuccess = true;
				sessionUser = u;
			}
		}
		
		try {
			if(!loginSuccess)
				username = (String) req.getSession().getAttribute("username");
			for (User u : userList) {
				if (u.getUsername().equals(username)) {
					loginSuccess = true;
					sessionUser = u;
				}
			}
		} catch (Exception e){
			//do nothing
		}
			
		if(loginSuccess) {
			Date curDate = new Date();
			SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss a zzz E dd/MM/yyyy", Locale.ENGLISH);
			String curTime = ft.format(curDate);
			ArrayList<Food> foodList = SQLiteFoodSelect.selectAllFood();
			
			req.getSession().setAttribute("username", sessionUser.getUsername());
			req.setAttribute("time", curTime);
			req.setAttribute("name", sessionUser.getName());
			req.setAttribute("type", sessionUser.getType());
			req.setAttribute("foodList", foodList);
			HashMap<String, Integer> lastSave = SQLiteMealSaveControl.selectUser(username);
			ArrayList<String> resutString = new ArrayList<String>();
			for(String s : lastSave.keySet()) {
				resutString.add(lastSave.get(s) + " of " + s);
			}
			req.setAttribute("lastSave", resutString);
			if(sessionUser.getType().equals("ADMIN")) {
				ArrayList<Food> unFoodList = SQLiteFoodSelect.selectAllUnpublishedFood();
				req.setAttribute("unFoodList", unFoodList);
				req.getRequestDispatcher("LoginSuccessStaff.jsp").forward(req, resp);
			} else
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
