<%@ page language="java" import="java.util.ArrayList, food.Food, database.SQLiteFoodSelect" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%
response.setHeader("Content-Language", "en");
%>
    
<!DOCTYPE html>
<html>
<head>
<meta  http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Successful!</title>
</head>
<body>
<p> Info of Your food </p>
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
	<p> Waiting for checking </p>
	<form method="post" action="login">
		<input type="submit" value="Back" />
	</form>

</body>
</html>