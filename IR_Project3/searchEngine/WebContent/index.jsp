<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
   
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Search Engine</title>

<style type="text/css">
.wapper{  
    position: relative;    
    height: auto;       
    min-height: 100% 
}  
.footer{  
   position: absolute;  
   bottom: 0;     
        
}  
.main-content{  
   padding-bottom: 60px; 
} 
.web-footer{
position: absolute;
left:350px; 
   bottom: 0; 
} 

   table {
      border-collapse: collapse;
   }
   th,td {
      border: 1px solid blue;
      padding-left: 7px;
   }
.btn.btn-default.pull-left{
-moz-box-shadow:inset 0px 1px 0px 0px #54a3f7;
	-webkit-box-shadow:inset 0px 1px 0px 0px #54a3f7;
	box-shadow:inset 0px 1px 0px 0px #54a3f7;
	background:-webkit-gradient(linear, left top, left bottom, color-stop(0.05, #007dc1), color-stop(1, #0061a7));
	background:-moz-linear-gradient(top, #007dc1 5%, #0061a7 100%);
	background:-webkit-linear-gradient(top, #007dc1 5%, #0061a7 100%);
	background:-o-linear-gradient(top, #007dc1 5%, #0061a7 100%);
	background:-ms-linear-gradient(top, #007dc1 5%, #0061a7 100%);
	background:linear-gradient(to bottom, #007dc1 5%, #0061a7 100%);
	filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#007dc1', endColorstr='#0061a7',GradientType=0);
	background-color:#007dc1;
	-moz-border-radius:3px;
	-webkit-border-radius:3px;
	border-radius:3px;
	border:1px solid #124d77;
	display:inline-block;
	cursor:pointer;
	color:#ffffff;
	font-family:arial;
	font-size:13px;
	padding:6px 24px;
	text-decoration:none;
	text-shadow:0px 1px 0px #154682;
}
   .btn.btn-default.pull-left:hover
{
  background:-webkit-gradient(linear, left top, left bottom, color-stop(0.05, #0061a7), color-stop(1, #007dc1));
	background:-moz-linear-gradient(top, #0061a7 5%, #007dc1 100%);
	background:-webkit-linear-gradient(top, #0061a7 5%, #007dc1 100%);
	background:-o-linear-gradient(top, #0061a7 5%, #007dc1 100%);
	background:-ms-linear-gradient(top, #0061a7 5%, #007dc1 100%);
	background:linear-gradient(to bottom, #0061a7 5%, #007dc1 100%);
	filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#0061a7', endColorstr='#007dc1',GradientType=0);
	background-color:#0061a7;
}
.btn.btn-default.pull-left:active {
  position:relative;
	top:1px;
}
.form-control{
line-height:2em;
}


</style>
</head>
<body>

<center>
<div id="wrapper">
	<div id = "main-content">	
	<h1>Search Engine</h1>
		<form action="result.jsp">
		<table bordercolor="#ffffff">
		<tr>
   			<td>
    			<input type="text" name="query" class="form-control" placeholder="Type query here" style="width: 400px"/>
  
   				<button type="submit" class="btn btn-default pull-left">
        			Search
   				</button></td>
   
		</tr>
		</form>
		</table>
	</div>
</div>
</center>
<footer class="web-footer">
      <hr>
      <p class="text-center" align = "center">
        © 2015 Information Retrieval Project
      </p>
    </footer>

</body>
</html>
