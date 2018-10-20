<%@ page language="java" import="java.util.ArrayList, food.Food, database.SQLiteFoodSelect" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%
response.setHeader("Content-Language", "en");
%>
    
<!DOCTYPE html>
<html>
<head>
<meta  http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<title>Successful!</title>
</head>
<header class="w3-container w3-teal">
<h1> Meal Planner </h1>
</header>
<body>
<div class="w3-container w3-half w3-margin-top">
<form class="w3-container w3-card-4" method="post" action="login">
<p> <h2>Info of Your food</h2> </p>
	<ul style="height: 300px; overflow: auto">
	<% 	Food newFood = (Food)request.getAttribute("newFood"); %>
	 Name: <%=newFood.getName() %> <br />
	 Energy: <%=newFood.getEnergy() %><br />
	 Protein: <%=newFood.getProtein() %><br />
	 Fat: <%=newFood.getFat() %> <br />
	 Sfa: <%=newFood.getSfa() %> <br />
	 Carb: <%=newFood.getCarb() %> <br />
	 Sugar: <%=newFood.getSugar() %> <br />
	 Sodium: <%=newFood.getSodium() %> <br />
	 Cost: <%=newFood.getCost() %> <br />

	</ul>
	<p> Waiting for our admin to censor! </p>
		<button class="w3-button w3-section w3-teal w3-ripple" type="submit" value="Back"> Back </button>
	</form>
</div>
</body>
</html>