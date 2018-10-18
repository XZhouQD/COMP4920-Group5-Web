<%@ page language="java" import="java.util.HashMap, java.util.ArrayList" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
	<p> Your target: Minimise ${target} </p>
	<p> Our suggestions: </p>
	<ul>
	<% ArrayList<String> result = (ArrayList<String>) request.getAttribute("result"); 
	for(String s : result) {%>
		<li> <%=s %></li>
	<%}%>
	</ul>
	<p> Hope you enjoy! </p>
</body>
</html>