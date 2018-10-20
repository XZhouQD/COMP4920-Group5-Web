<%@ page language="java" import="java.util.ArrayList, food.Food, database.SQLiteFoodSelect" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%
response.setHeader("Content-Language", "en");
%>
    
<!DOCTYPE html>
<html>
<head>
<meta  http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Successfully added new meal!</title>
</head>
<body>
<p> Info of Your food </p>
	<ul>
	<% 	Food newMeal = (Food)request.getAttribute("newMeal"); %>
	 Name: <%=newMeal.getName() %> <br />
	 Energy: <%=newMeal.getEnergy() %><br />
	 Protein: <%=newMeal.getProtein() %><br />
	 Fat: <%=newMeal.getFat() %> <br />
	 Sfa: <%=newMeal.getSfa() %> <br />
	 Carb: <%=newMeal.getCarb() %> <br />
	 Sugar: <%=newMeal.getSugar() %> <br />
	 Sodium: <%=newMeal.getSodium() %> <br />
	 Cost: <%=newMeal.getCost() %> <br />

	</ul>
	<p> </p>
	<form method="post" action="login">
		<input type="submit" value="Back" />
	</form>

</body>
</html>