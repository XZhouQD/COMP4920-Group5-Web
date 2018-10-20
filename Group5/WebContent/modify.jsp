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

<div class="w3-panel w3-light-grey w3-padding-16 w3-card">
<div class="w3-content" style="max-width:800px">
<h2> Published Food list </h2>
 <form method="post" action="modifymeal">
		<ul>
		<%
		ArrayList<Food> foods = SQLiteFoodSelect.selectAllFood();
		for(Food f : foods) { %>
			<li> <input type="radio" name="reserved" value="<%=f.getName() %>"> <input type="text" name="<%=f.getName() %>"> <%=f.toString() %></li>
		<% }%>
	</ul>
	


<h2>Unpublished Food list</h2>
	<ul>
	<%
		ArrayList<Food> food = SQLiteFoodSelect.selectAllUnpublishedFood();
		for(Food f : food) { %>
			<li> <input type="radio" name="reserved" value="<%=f.getName() %>"> <input type="text" name="<%=f.getName() %>"> <%=f.toString() %></li>
		<% }%>
		<!-- <li> <input type="radio" name="reserved" value="None" checked="checked"/> -->
	</ul>
	<h2>Enter new info</h2>
	Energy: <input type="text" name="energy" /><br />
	Protein: <input type="text" name="protein" /><br/>
	Fat: <input type="text" name="fat" /><br/>
	Sfa: <input type="text" name="sfa" /><br/>
	Carb: <input type="text" name="carb" /><br/>
	Sugar: <input type="text" name="sugar" /><br/>
	Sodium: <input type="text" name="sodium" /><br/>
	Cost: <input type="text" name="cost" /><br/>

	<input type="submit" class="w3-button w3-padding-large w3-dark-grey" style="width:98.5%" value="Confirm" />
	</form>

<form method="post" action="login">
<input type="submit" class="w3-button w3-padding-large w3-dark-grey" style="width:98.5%" value="Back" />
</form>
</div>
</div>

<p> ${message} </p>

</body>
</html>