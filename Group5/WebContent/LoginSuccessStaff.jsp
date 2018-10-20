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
	<p>Published Food List:</p>
	<ul>
	<%
	ArrayList<Food> foods = (ArrayList<Food>) request.getAttribute("foodList");
	for(Food f : foods) { %>
		<li> <%=f.toString() %> </li>
	<% }
	%>
	</ul>
	
	
	<p>Unpublished Food List:</p>
	<ul>
	<%
	ArrayList<Food> unFoods = (ArrayList<Food>) request.getAttribute("unFoodList");
	for(Food f : unFoods) { %>
		<li> <%=f.toString() %> </li>
	<% }
	%>
	</ul>
</body>
</html>