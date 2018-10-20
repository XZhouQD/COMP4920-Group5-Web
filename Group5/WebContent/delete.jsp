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
<p> Published Food list </p>
<p>Food List:</p>
 <form method="post" action="deletemeal">
		<ul>
		<%
		ArrayList<Food> foods = SQLiteFoodSelect.selectAllFood();
		for(Food f : foods) { %>
			<li> <input type="radio" name="reserved" value="<%=f.getName() %>"> <input type="text" name="<%=f.getName() %>"> <%=f.toString() %></li>
		<% }%>
	</ul>
	


<p>Unpublished Food list</p>
<p> Food List:</p>
	<ul>
	<%
		ArrayList<Food> food = SQLiteFoodSelect.selectAllUnpublishedFood();
		for(Food f : food) { %>
			<li> <input type="radio" name="reserved" value="<%=f.getName() %>"> <input type="text" name="<%=f.getName() %>"> <%=f.toString() %></li>
		<% }%>
		<li> <input type="radio" name="reserved" value="None" checked="checked"/>
	</ul>
	<input type="submit" value="Confirm" />
</form>

<form method="post" action="login">
<input type="submit" value="Back" />
</form>

<p> ${message} </p>

</body>
</html>