<%@ page language="java" import="java.util.ArrayList, food.Food" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Meal Suggestions</title>
</head>
<body>
	<p>Session Time: ${time}</p>
	<p>Welcome to meal suggestions, ${name}!</p>
	<p>Food List:</p>
	<ul>
	<%
	ArrayList<Food> foods = (ArrayList<Food>) request.getAttribute("foodList");
	for(Food f : foods) { %>
		<li> <%=f.toString() %> </li>
	<% }
	%>
	</ul>
</body>
</html>