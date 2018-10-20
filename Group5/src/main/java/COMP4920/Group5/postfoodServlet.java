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

public class postfoodServlet extends HttpServlet  {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String username = (String)req.getSession().getAttribute("username");
		String name = (String)req.getParameter("foodName");
		double energy = Double.parseDouble(req.getParameter("energy"));
		double protein = Double.parseDouble(req.getParameter("protein"));
		double fat = Double.parseDouble(req.getParameter("fat"));
		double sfa = Double.parseDouble(req.getParameter("sfa"));
		double carb = Double.parseDouble(req.getParameter("carb"));
		double sugar = Double.parseDouble(req.getParameter("suger"));
		double sodium = Double.parseDouble(req.getParameter("sodium"));
		double cost = Double.parseDouble(req.getParameter("cost"));
		
		Food newFood = new Food(name, energy, protein, fat, sfa, carb, sugar, sodium,cost);
		
		SQLiteInsertFood.insertUnpublishedFood(newFood, username);
		//ArrayList<Food> unFoodList = SQLiteFoodSelect.selectAllUnpublishedFood();
		
		req.setAttribute("newFood", newFood);
		req.getRequestDispatcher("postSuccess.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
