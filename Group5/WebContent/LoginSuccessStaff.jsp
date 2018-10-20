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
  <form method="post" action="meal">
  	<input class="w3-bar-item w3-button w3-teal" type="submit" value="Add meal" />
  </form>
  <form method="post" action="delete">
  	<input class="w3-bar-item w3-button w3-teal" type="submit" value="Delete meal" />
  </form>
  <form method="post" action="modify">
  	<input class="w3-bar-item w3-button w3-teal" type="submit" value="Modify meal" />
  </form>
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

<hr>
<h2>Food List</h2>

<div class="w3-panel w3-light-grey w3-padding-16 w3-card">
<div class="w3-content" style="max-width:800px">
		<ul>
		<%
		ArrayList<Food> foods = SQLiteFoodSelect.selectAllFood();
		for(Food f : foods) { %>
		<li> <%=f.toString() %> </li>
		<% }
		%>
		</ul>	
</div>
</div>
</div>

<div class="w3-container" style="padding:32px">
<h2>Unpublished food list</h2>

<div class="w3-panel w3-light-grey w3-padding-16 w3-card">
<div class="w3-content" style="max-width:800px">
	<ul>
	<%
	ArrayList<Food> unFoods = (ArrayList<Food>) request.getAttribute("unFoodList");
	for(Food f : unFoods) { %>
		<li> <%=f.toString() %> </li>
	<% }
	%>
	</ul>
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
