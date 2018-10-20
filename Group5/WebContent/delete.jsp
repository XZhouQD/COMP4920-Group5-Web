<%@ page language="java" import="java.util.ArrayList, food.Food, database.SQLiteFoodSelect" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
response.setHeader("Content-Language", "en");
%>
<!DOCTYPE html>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<title>Meal Suggestions</title>
</head>
<header class="w3-container w3-teal">
  <h1>Meal Planner </h1>
</header>

<body>
<h2> Published Food list </h2>
<div class="w3-panel w3-light-grey w3-padding-16 w3-card">
<div class="w3-content" style="max-width:800px">
 <form method="post" action="deletemeal">
		<ul>
		<%
		ArrayList<Food> foods = SQLiteFoodSelect.selectAllFood();
		for(Food f : foods) { %>
			<li> <input type="radio" name="reserved" value="<%=f.getName() %>"> <input type="text" name="<%=f.getName() %>"> <%=f.toString() %></li>
		<% }%>
	</ul>
	<div class="w3-row">
  	<div class="w3-col m6">
	<input type="submit" class="w3-button w3-padding-large w3-dark-grey" style="width:98.5%" value="Confirm" />
	</form>
	</div>
	</div>
</div>
</div>


<h2>Unpublished Food list</h2>
<div class="w3-panel w3-light-grey w3-padding-16 w3-card">
<div class="w3-content" style="max-width:800px">
 <form method="post" action="deletemeal">
	<ul>
	<%
		ArrayList<Food> food = SQLiteFoodSelect.selectAllUnpublishedFood();
		for(Food f : food) { %>
			<li> <input type="radio" name="reserved" value="<%=f.getName() %>"> <input type="text" name="<%=f.getName() %>"> <%=f.toString() %></li>
		<% }%>
		<!--  <li> <input type="radio" name="reserved" value="None" checked="checked"/> -->
	</ul>
	<div class="w3-row">
	<div class="w3-col m6">
	<input type="submit" class="w3-button w3-padding-large w3-dark-grey" style="width:98.5%" value="Confirm" />
	</div>
	</div>
</form>
</div>
</div>

<form method="post" action="login">
<input type="submit" class="w3-button w3-padding-large w3-dark-grey" style="width:98.5%" value="Back" />
</form>

<p> ${message} </p>
</body>
</html>