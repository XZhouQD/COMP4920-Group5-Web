<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Meal Suggestions</title>
</head>
<body>
	<form method="post" action="register">
		Username: <input type="text" name="username" /><br />
		Password: <input type="password" name="password" /><br />
		Repeat: <input type="password" name="repeatPassword" /><br />
		Name: <input type="text" name="name" /><br />
		<input type="submit" value="Register" />
	</form>
	<form method="post" action="login">
		<input type="submit" value="Back" />
	</form>
	<p> ${message} </p>
</body>
</html>