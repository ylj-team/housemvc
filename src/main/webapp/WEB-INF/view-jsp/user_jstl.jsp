<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>


<html>
<head>
<title>Login</title>

</head>
<body>
	<h3>
		<a href="./user" ><c:out value="=>${account}" /></a>
		
		<a href="./loginpasswordchange.html?account=${account}" >修改密码</a>
		<a href="./do_logout" >logout</a>
	</h3>
	
	
	<h2>
		<c:out value="-----------------------------------" />
	</h2>
	
	<p>
		<a href="./daily_jstl" >每日签约</a>
	</p>
	
	<h2 hidden>
		account
		<c:out value="=>${account}" />
	</h2>
	
	<h2 hidden>
		passwd
		<c:out value="=>${passwd}" />
	</h2>
	
	<h2 hidden>
		nextUrl
		<c:out value="=>${nextUrl}" />
	</h2>
</body>
</html>