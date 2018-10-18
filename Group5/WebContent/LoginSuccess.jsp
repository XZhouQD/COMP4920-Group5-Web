<%@ page language="java" import="java.util.ArrayList, food.Food" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
	ArrayList<Food> foods = (ArrayList<Food>) request.getAttribute("foodList");
	for(Food f : foods) { %>
		<li> <input type="checkbox" name="reserved" value="<%=f.getName() %>"> <input type="text" name="<%=f.getName() %>"> <%=f.toString() %></li>
	<% }
	%>
	</ul>
	<div>
	<input id="minimise" name="minimise" type="radio" value="Cost" checked="checked" /> Cost
	<input id="minimise" name="minimise" type="radio" value="Energy"/> Energy
	<input id="minimise" name="minimise" type="radio" value="Protein"/> Protein
	<input id="minimise" name="minimise" type="radio" value="Fat"/> Fat
	<input id="minimise" name="minimise" type="radio" value="Sfa"/> Sfa
	<input id="minimise" name="minimise" type="radio" value="Carb"/> Carb
	<input id="minimise" name="minimise" type="radio" value="Sugar"/> Sugar
	<input id="minimise" name="minimise" type="radio" value="Sodium"/> Sodium	
	</div>
	Use Checkbox to reserve at least 1 serve to have!
	<input type="submit" value="minimise" />
	</form>
</body>
</html>