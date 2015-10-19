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
	
	
	<h2>
		<c:out value="-----------------------------------" />	
	</h2>
	<h3>		
		楼盘查找
	</h3>
	
	<p>		
			
		<form name="login_input" action="./do_PropertySearch_jstl" method="get"  accept-charset="utf-8" onsubmit="document.charset='utf-8';">	
		 	<input type="text" name="query" value="${query}"/>	
			<input type="submit" value="搜索" />	
		</form>
	
	</p>
	
	
	<h2>
		<c:out value="-----------------------------------" />	
	</h2>
	
</body>
</html>