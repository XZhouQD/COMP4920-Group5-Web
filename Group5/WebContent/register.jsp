<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<title>Meal Suggestions</title>
</head>
<header class="w3-container w3-teal">
  <h1>Register </h1>
</header>

<div class="w3-container w3-half w3-margin-top">
	<form class="w3-container w3-card-4" method="post" action="register">
		<p>
		<input class="w3-input" type="text" style="width:90%" required type="text" name="username" />
		<label>Username</label></p>
		<p>
		<input class="w3-input" type="password" style="width:90%" required type="password" name="password" />
		<label>Password</label></p>
		<p>
		<input class="w3-input" type="password" style="width:90%" required type="password" name="repeatPassword" />
		<label>Repeat your Password</label></p>
		<p>
		<input class="w3-input" type="text" style="width:90%" required type="text" name="name" />
		<label>Name</label></p>
		<p>
		<button class="w3-button w3-section w3-teal w3-ripple" type="submit" value="Register"> Register </button></p>
	</form>
	<form class="w3-container w3-card-4" method="post" action="login">
		<button class="w3-button w3-section w3-teal w3-ripple" type="submit" value="Back"> Back </button></p>
	</form>
	<p> ${message} </p>
</div>

</html>