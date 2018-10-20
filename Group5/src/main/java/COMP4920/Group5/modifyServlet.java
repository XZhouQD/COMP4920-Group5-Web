package COMP4920.Group5;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.SQLiteFoodSelect;
import database.SQLiteInsertFood;
import database.SQLiteUserSelect;
import food.Food;

public class modifyServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String[] targetList = req.getParameterValues("reserved").clone();
		String foodToModify = targetList[0];
		double energy= -1;
		double protein= -1;
		double fat= -1;
		double sfa= -1;
		double carb= -1;
		double sugar= -1;
		double sodium= -1;
		double cost= -1;
		
		if(foodToModify.equals("None"))
		{
			System.out.println(foodToModify+ " ");
			
			req.setAttribute("message", "Please provide name of the meal to delete");
			req.getRequestDispatcher("delete.jsp").forward(req, resp);
			
		} else {
			try {
				energy = Double.parseDouble(req.getParameter("energy"));
			}catch (Exception e) {
			}
			try {
				protein = Double.parseDouble(req.getParameter("protein"));
			}catch (Exception e) {
				}
			try {
				fat = Double.parseDouble(req.getParameter("fat"));
			}catch (Exception e) {
			}
			try {
				sfa = Double.parseDouble(req.getParameter("sfa"));
			}catch(Exception e) {
			}
			try {
				carb = Double.parseDouble(req.getParameter("carb"));
			}catch (Exception e) {
			}
			try {
				sugar = Double.parseDouble(req.getParameter("sugar"));
			}catch(Exception e) {
			}
			try {
				sodium = Double.parseDouble(req.getParameter("sodium"));
			}catch(Exception e) {
			}
			try {
				cost = Double.parseDouble(req.getParameter("cost"));
			} catch (Exception e) {
			}
			
			
			Food toModify = SQLiteFoodSelect.getFoodByName(foodToModify);
			if(energy!=-1) {
				toModify.setEnergy(energy);
			}
			if(protein!=-1) {
				toModify.setProtein(protein);
			}
			if(fat!=-1) {
				toModify.setFat(fat);
			}
			if(sfa!=-1) {
				toModify.setSfa(sfa);
			}
			if(carb!=-1) {
				toModify.setCarb(carb);
			}
			if(sugar!=-1) {
				toModify.setSugar(sugar);
			}
			if(sodium!=-1) {
				toModify.setSodium(sodium);
			}
			if(cost!=-1) {
				toModify.setCost(cost);
			}
			
			toModify.updateFood();
			
			req.getRequestDispatcher("modifySuccess.jsp").forward(req, resp);
		System.out.println(foodToModify+ " ");
			
				
			}
			
			
	}

		@Override
		protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			doGet(req, resp);
		}
		
}
