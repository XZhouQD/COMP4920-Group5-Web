<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Meal Suggestions</title>
</head>

<body>
<form method="post" action="addmeal">
	NAME: <input type="text" name="name" /><br />
	ENERGY: <input type="text" name="energy" /><br />
	PROTEIN: <input type="text" name="protein" /><br/>
	FAT: <input type="text" name="fat" /><br/>
	SFA: <input type="text" name="sfa" /><br/>
	CARB: <input type="text" name="carb" /><br/>
	SUGAR: <input type="text" name="sugar" /><br/>
	SODIUM: <input type="text" name="sodium" /><br/>
	COST: <input type="text" name="cost" /><br/>
	<input type="submit" value="Confirm" />
</form>

<form method="post" action="login">
<input type="submit" value="Back" />
</form>

<p> ${message} </p>

</body>
</html>