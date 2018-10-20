<%@ page language="java" import="java.util.HashMap, java.util.ArrayList" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
  <h1> Meal Planner </h1>
</header>

<div class="w3-container w3-half w3-margin-top">
<form class="w3-container w3-card-4" method="post" action="login">
	<p> Your target: Minimise ${target} </p>
	<p> Our suggestions: </p>
	<ul style="height: 300px; overflow: auto">
	<% ArrayList<String> result = (ArrayList<String>) request.getAttribute("result"); 
	for(String s : result) {%>
		<li> <%=s %></li>
	<%}%>
	</ul>
	<p> Hope you enjoy! </p>
		<button class="w3-button w3-section w3-teal w3-ripple" type="submit" value="Back"> Back </button>
	</form>
	
</div>
</html>