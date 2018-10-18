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

import database.SQLiteFoodSelect;
import database.SQLiteUserSelect;
import food.Food;
import linearProgramming.LpWizardTry;
import user.User;

public class minimiseServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String[] reserve = req.getParameterValues("reserved");
		HashMap<String, Integer> reserveList = new HashMap<String, Integer>();
		for(String s : reserve) {
			int reserveSize = 1;
			try {
				reserveSize = Integer.parseInt(req.getParameter(s));
			} catch (Exception e) {
				reserveSize = 1;
			}
			if(reserveSize < 1)
				reserveSize = 1;
			System.out.println(s);
			reserveList.put(s, reserveSize);
		}
		String[] target = req.getParameterValues("minimise");
		String specific = "Cost";
		for(String s : target) {
			if (s != null && !s.equals(""))
				specific = s;
		}
		
		ArrayList<Food> fList = SQLiteFoodSelect.selectAllFood();

		LpWizardTry lpwT = new LpWizardTry(fList, specific, reserveList);
		HashMap<String, Integer> result = lpwT.getLowestCombo();
		String[] output = new String[result.size()];
		int i = 0;
		for(String s : result.keySet()) {
			if(result.get(s) <= 1)
				output[i] = result.get(s) + " serve of " + s;
			else
				output[i] = result.get(s) + " serves of " + s;
			i++;
		}
		
		req.setAttribute("target", specific);
		req.setAttribute("result", output);
		
		req.getRequestDispatcher("suggestions.jsp").forward(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
