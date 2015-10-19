<%@ page language="java" pageEncoding="UTF-8"  contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>


<html>
<head>
<title>PropertySearch</title>


<style>
.div-a {
	float: left;
	width: 99%;
	border: 1px solid #000
}


</style>

</head>
<body>
	<h2>
		<a href="./user" ><c:out value="=>${account}" /></a>
		<a href="./do_logout" >logout</a>
	
	</h2>

	<form name="login_input" action="./do_PropertySearch_jstl" method="get"  accept-charset="utf-8" onsubmit="document.charset='utf-8';">
		
		<p>
			query : <input type="text" name="query" value="${query}"/>
		</p>
		
		<input type="submit" value="Submit" />

		
	</form>
	<c:out value="-中文" />
	<c:if test="${not empty properties}">

		<ul>
			<c:forEach var="property" items="${properties}">
				
				<HR align=center width=100% color=#987cb9 SIZE=1>
				
				<li>
					<h3>
						<c:out value="------------------------" />
					</h3> 
					
					<div class="div-a">
						${property.propertyName}	
								
					</div>
					

				</li>
				
			
				
			</c:forEach>
		</ul>

	</c:if>


</body>
</html>