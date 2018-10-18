<%@ page language="java" import="java.util.ArrayList, food.Food, database.SQLiteFoodSelect" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
response.setHeader("Content-Language", "en");
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Meal Suggestions</title>
</head>
<body>
	<p>Session Time: ${time}</p>
	<p>Welcome to meal suggestions, ${name}!</p>
	<p>You are ${type} of the site.</p>
	<form method="post" action="logout">
		<input type="submit" value="Logout" />
	</form>
	<p>Food List:</p>
	<form method="post" action="minimise">
		<ul>
		<%
		ArrayList<Food> foods = SQLiteFoodSelect.selectAllFood();
		for(Food f : foods) { %>
			<li> <input type="checkbox" name="reserved" value="<%=f.getName() %>"> <input type="text" name="<%=f.getName() %>"> <%=f.toString() %></li>
		<% }%>
		</ul>
		<input id="minimise" name="minimise" type="radio" value="Cost" checked="checked" /> Cost
		<input id="minimise" name="minimise" type="radio" value="Energy"/> Energy
		<input id="minimise" name="minimise" type="radio" value="Protein"/> Protein
		<input id="minimise" name="minimise" type="radio" value="Fat"/> Fat
		<input id="minimise" name="minimise" type="radio" value="Sfa"/> Sfa
		<input id="minimise" name="minimise" type="radio" value="Carb"/> Carb
		<input id="minimise" name="minimise" type="radio" value="Sugar"/> Sugar
		<input id="minimise" name="minimise" type="radio" value="Sodium"/> Sodium	
		<input type="submit" value="Minimise" />
	</form>
	Use Checkbox to reserve at least 1 serve to have!
	<p>Last Optimisation:</p>
	<ul>
	<%
		ArrayList<String> lastSave = (ArrayList<String>) request.getAttribute("lastSave");
		for(String s : lastSave) { %>
		<li> <%=s %> </li>
	<%} %>
	</ul>
</body>
</html>