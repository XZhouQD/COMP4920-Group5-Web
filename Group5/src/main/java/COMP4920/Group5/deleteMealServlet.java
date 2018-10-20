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

public class deleteMealServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String[] targetList = req.getParameterValues("reserved").clone();
		String foodToDelete = targetList[0];
		
		if(foodToDelete.equals("None"))
		{
			System.out.println(foodToDelete+ " ");
			
			req.setAttribute("message", "Please provide name of the meal to delete");
			req.getRequestDispatcher("delete.jsp").forward(req, resp);
			
		} else {
			
			SQLiteFoodSelect.deleteFoodByName(foodToDelete);
			SQLiteFoodSelect.deleteFoodByNameUnpublished(foodToDelete);
			
			req.getRequestDispatcher("deletemealSuccess.jsp").forward(req, resp);
		System.out.println(foodToDelete+ " ");
			
				
			}
			
			
	}

		@Override
		protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			doGet(req, resp);
		}
		
}
