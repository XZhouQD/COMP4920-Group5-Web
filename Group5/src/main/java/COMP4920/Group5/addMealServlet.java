package COMP4920.Group5;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
boolean addmealSuccess = true;
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
addmealSuccess = false;
req.setAttribute("message", "Please provide all the values");
}
System.out.println();
}

}

