package COMP4920.Group5;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.ServletException;
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
		String[] rFoodList = req.getParameterValues("reserved");
		String[] targetList = req.getParameterValues("minimise");
		
		ArrayList<Food> fList = SQLiteFoodSelect.selectAllFood();
		HashMap<String, Integer> reserve = new HashMap<String, Integer>();
		
		for(String s : rFoodList) {
			reserve.put(s, Integer.parseInt(req.getParameter(s)));
		}
		
		LpWizardTry lpwT = new LpWizardTry(fList, targetList[0], reserve);
		HashMap<String, Integer> result = lpwT.getLowestCombo();
		ArrayList<String> resutString = new ArrayList<String>();
		for(String s : result.keySet()) {
			resutString.add(result.get(s) + " of " + s);
		}
		
		req.setAttribute("target", targetList[0]);
		req.setAttribute("result", resutString);
		req.getRequestDispatcher("suggestions.jsp").forward(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
