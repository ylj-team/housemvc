<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>


<html>
<head>
<title>Login</title>

</head>
<body>
	<h2>
		checkResult
		<c:out value="=>${checkResult}" />
	</h2>

	<h2>
		account
		<c:out value="=>${account}" />
	</h2>
	
	<h2>
		passwd
		<c:out value="=>${passwd}" />
	</h2>
	<h2>
		nextUrl
		<c:out value="=>${nextUrl}" />
	</h2>
</body>
</html>