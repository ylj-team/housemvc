<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>


<html>
<head>
<title>Login</title>

</head>
<body>
	<h2>
		<c:out value="=>${account}" />
		<a href="./do_logout" >logout</a>
	</h2>
	
	
	<h2>
		<c:out value="登陆成功" />
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