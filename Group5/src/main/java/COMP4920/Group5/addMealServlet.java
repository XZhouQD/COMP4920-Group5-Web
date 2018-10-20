package COMP4920.Group5;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.SQLiteInsertFood;
import database.SQLiteUserSelect;
import food.Food;

public class addMealServlet extends HttpServlet {
@Override
protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException{
		
		String name= null;
		double energy= 0.00;
		double protein= 0.00;
		double fat= 0.00;
		double sfa= 0.00;
		double carb= 0.00;
		double sugar= 0.00;
		double sodium= 0.00;
		double cost= 0.00;
		
		try {
			name = req.getParameter("name");
			energy = Double.parseDouble(req.getParameter("energy"));
			protein = Double.parseDouble(req.getParameter("protein"));
			fat = Double.parseDouble(req.getParameter("fat"));
			sfa = Double.parseDouble(req.getParameter("sfa"));
			carb = Double.parseDouble(req.getParameter("carb"));
			sugar = Double.parseDouble(req.getParameter("sugar"));
			sodium = Double.parseDouble(req.getParameter("sodium"));
			cost = Double.parseDouble(req.getParameter("cost"));
		} catch (Exception e) {
			req.setAttribute("message", "Please provide all the values");
			req.getRequestDispatcher("meal.jsp").forward(req, resp);
		}
		
			System.out.println(name+ " ");
				if(name==null || cost == 0) {
					req.setAttribute("message", "Please provide all the details");
					req.getRequestDispatcher("meal.jsp").forward(req, resp);
				}
				
			Food newMeal = new Food(name,energy,protein,fat,sfa,carb,sugar,sodium,cost);
			SQLiteInsertFood.insertFood( newMeal);
			
			req.setAttribute("newMeal", newMeal);
			req.getRequestDispatcher("addMealSuccess.jsp").forward(req, resp);
			}

		@Override
		protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			doGet(req, resp);
		}

}

