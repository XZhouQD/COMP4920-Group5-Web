<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Meal Suggestions</title>
</head>
<body>
	<form method="post" action="/Group5/login">
		Username: <input type="text" name="username" /><br />
		Password: <input type="password" name="password" /><br />
		<input type="submit" value="Login" />
	</form>
	<form method="post" action="/Group5/register">
		<input type="submit" value="Register" />
	</form>
	<p> ${message} </p>
</body>
</html>