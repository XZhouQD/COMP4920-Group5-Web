package COMP4920.Group5;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.*;
import food.Food;
import linearProgramming.LpWizardTry;
import user.User;

public class minimiseServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String[] rFoodList;
		String[] targetList = req.getParameterValues("minimise").clone();
		String username = (String) req.getSession().getAttribute("username");
		
		ArrayList<Food> fList = SQLiteFoodSelect.selectAllFood();
		HashMap<String, Integer> reserve = new HashMap<String, Integer>();
		
		try {
			rFoodList = req.getParameterValues("reserved").clone();
		} catch (Exception e) {
			rFoodList = new String[1];
			rFoodList[0]="null";
		}

		for(int i = 0; i < rFoodList.length; i++) {
				if(rFoodList[i].equals("null")) break;
				reserve.put(rFoodList[i], Integer.parseInt(req.getParameter(rFoodList[i])));
		}
		
		LpWizardTry lpwT = new LpWizardTry(fList, targetList[0], reserve);
		HashMap<String, Integer> result = lpwT.getLowestCombo();
		SQLiteMealSaveControl.insertUser(username, result);
		ArrayList<String> resutString = new ArrayList<String>();
		for(String s : result.keySet()) {
			if(result.get(s) > 0)
				resutString.add(result.get(s) + " of " + s);
		}
		
		req.setAttribute("target", targetList[0]);
		req.setAttribute("result", resutString);
		RequestDispatcher rs = req.getRequestDispatcher("suggestions.jsp");
		rs.forward(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
