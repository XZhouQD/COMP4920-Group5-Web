<%@ page language="java" import="java.util.ArrayList, food.Food, database.SQLiteFoodSelect" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
response.setHeader("Content-Language", "en");
%>
<!DOCTYPE html>
<html>
<title>Meal Planner</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" href="https://www.w3schools.com/lib/w3-theme-teal.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<style>
body {font-family: "Roboto", sans-serif}
.w3-bar-block .w3-bar-item{padding:16px;font-weight:bold}
</style>
<body>

<nav class="w3-sidebar w3-bar-block w3-collapse w3-animate-left w3-card" style="z-index:3;width:250px;" id="mySidebar">
  <a class="w3-bar-item w3-button w3-border-bottom w3-large" href="#"><img src="https://www.resortdata.com/wp-content/uploads/2016/05/meal-planning-icon-300x242.png" style="width:80%;"></a>
  <a class="w3-bar-item w3-button w3-hide-large w3-large" href="javascript:void(0)" onclick="w3_close()"> Close <i class="fa fa-remove"></i></a>
  <a class="w3-bar-item w3-button w3-teal" href="#">Home</a>
  <a class="w3-bar-item w3-button w3-teal" href="#">${time}</a>
  <a class="w3-bar-item w3-button w3-teal" href="#">You are ${type} of the site.</a>
  <form method="post" action="logout">
		<input class="w3-bar-item w3-button w3-teal" type="submit" value="Logout" />
  </form>
</nav>

<div class="w3-overlay w3-hide-large w3-animate-opacity" onclick="w3_close()" style="cursor:pointer" id="myOverlay"></div>

<div class="w3-main" style="margin-left:250px;">

<div id="myTop" class="w3-container w3-top w3-theme w3-large">
  <p><i class="fa fa-bars w3-button w3-teal w3-hide-large w3-xlarge" onclick="w3_open()"></i>
  <span id="myIntro" class="w3-hide">Meal Planner</span></p>
</div>

<header class="w3-container w3-theme" style="padding:64px 32px">
  <h1 class="w3-xxxlarge">Meal Planner</h1>
</header>

<div class="w3-container" style="padding:32px">

<h2>What is Meal Planner</h2>

<ul class="w3-leftbar w3-theme-border" style="list-style:none">
 <li>Meal Planner is a tool of managing your meals</li>
</ul>
<br>

<img src="https://wellnessmama.com/wp-content/uploads/meal-planning-basics-how-to-meal-plan.jpg" style="width:100%" alt="Responsive">


<h2>Easy to Use</h2>
<div class="w3-container w3-sand w3-leftbar">
<p><i>Step 1: Go through our food list and pick whatever you want to have.</i><br></p>
<p><i>Step 2: Choose a nutrition ingredient(or cost) that you want to minimize. </i><br></p>
<p><i>Step 3: Click the button and wait magic happen.</i><br></p>
</div>

<hr>
<h2>Food List</h2>

<p>We have created some foods for you to choose.</p>
<p>You are free to add new food to the list.</p>


<div class="w3-panel w3-light-grey w3-padding-16 w3-card">
<div class="w3-content" style="max-width:800px">
	<form method="post" action="minimise">
		<ul>
		<%
		ArrayList<Food> foods = SQLiteFoodSelect.selectAllFood();
		for(Food f : foods) { %>
			<li> <input type="checkbox" name="reserved" value="<%=f.getName() %>"> <input type="text" name="<%=f.getName() %>"> <%=f.toString() %></li>
		<% }%>
		</ul>
		<p>Use Checkbox to reserve at least 1 serve to have!</p>
		<input id="minimise" name="minimise" type="radio" value="Cost" checked="checked" /> Cost
		<input id="minimise" name="minimise" type="radio" value="Energy"/> Energy
		<input id="minimise" name="minimise" type="radio" value="Protein"/> Protein
		<input id="minimise" name="minimise" type="radio" value="Fat"/> Fat
		<input id="minimise" name="minimise" type="radio" value="Sfa"/> Sfa
		<input id="minimise" name="minimise" type="radio" value="Carb"/> Carb
		<input id="minimise" name="minimise" type="radio" value="Sugar"/> Sugar
		<input id="minimise" name="minimise" type="radio" value="Sodium"/> Sodium	
		<div class="w3-row">
  		<div class="w3-col m6">
    	<input type="submit" class="w3-button w3-padding-large w3-dark-grey" style="width:98.5%" value="Minimise">
  		</div>
  		</div>
	</form>
	
</div>
</div>
</div>


<div class="w3-container" style="padding:32px">
<h2>Last Optimisation:</h2>

<ul class="w3-leftbar w3-theme-border" style="list-style:none">
	<%
		ArrayList<String> lastSave = (ArrayList<String>) request.getAttribute("lastSave");
		for(String s : lastSave) { %>
		<li> <%=s %> </li>
	<%} %>	
</ul>

<hr>
<h2>Post food</h2>

<p>Add your favorite food to our food list</p>

<div class="w3-panel w3-light-grey w3-padding-16 w3-card">
<div class="w3-content" style="max-width:800px">
	<form method = "post" action = "postfood">
		FoodName:<input type="text" name="foodName" /><br />
		Energy:  <input type="text" name="energy" /><br />
		Protein:  <input type="text" name="protein" /><br />
		Fat: 	  <input type="text" name="fat" /><br />
		Sfa:      <input type="text" name="sfa" /><br />
		Carb:     <input type="text" name="carb" /><br />
		Sugar:    <input type="text" name="suger" /><br />
		Sodium:   <input type="text" name="sodium" /><br />
		Cost:     <input type="text" name="cost" /><br />
		<div class="w3-row">
  		<div class="w3-col m6">
    	<input type="submit" class="w3-button w3-padding-large w3-dark-grey" style="width:98.5%" value="Postfood">
  		</div>
  		</div>
	</form>
</div>
</div>
</div>


<footer class="w3-container w3-theme" style="padding:32px">
  <p>Comp4920 Group5</p>
</footer>
     
</div>

<script language= javascript>
// Open and close the sidebar on medium and small screens
function w3_open() {
    document.getElementById("mySidebar").style.display = "block";
    document.getElementById("myOverlay").style.display = "block";
}
function w3_close() {
    document.getElementById("mySidebar").style.display = "none";
    document.getElementById("myOverlay").style.display = "none";
}

// Change style of top container on scroll
window.onscroll = function() {myFunction()};
function myFunction() {
    if (document.body.scrollTop > 80 || document.documentElement.scrollTop > 80) {
        document.getElementById("myTop").classList.add("w3-card-4", "w3-animate-opacity");
        document.getElementById("myIntro").classList.add("w3-show-inline-block");
    } else {
        document.getElementById("myIntro").classList.remove("w3-show-inline-block");
        document.getElementById("myTop").classList.remove("w3-card-4", "w3-animate-opacity");
    }
}

// Accordions
function myAccordion(id) {
    var x = document.getElementById(id);
    if (x.className.indexOf("w3-show") == -1) {
        x.className += " w3-show";
        x.previousElementSibling.className += " w3-theme";
    } else { 
        x.className = x.className.replace("w3-show", "");
        x.previousElementSibling.className = 
        x.previousElementSibling.className.replace(" w3-theme", "");
    }
}
</script>
     
</body>
</html> 
