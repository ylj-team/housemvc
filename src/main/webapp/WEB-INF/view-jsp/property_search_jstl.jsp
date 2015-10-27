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
		
		
		<input type="text" name="query" value="${query}"/>	
		
		<input type="submit" value="搜索" />

		
	</form>


		<HR align=center width=100% color=#987cb9 SIZE=1>
				<p>
		<c:out value="命中结果：${searchResultSize}" />
	</p>
		 
	<c:if test="${not empty searchResults}">


		<ul>
			<c:forEach var="searchRecord" items="${searchResults}">
				
			
				<li>
					<h3>
						<c:out value="------------------------" />
					</h3> 
					
				<!--  	<div class="div-a"> -->
						
					<ul>
			
						<li><a href="${searchRecord.jumpUrl}" target="_blank">${searchRecord.propertyName}</a></li>
					
						<li>${searchRecord.city}</li>
						<li>${searchRecord.addr}</li>
						<li>${searchRecord.developer}</li>
						
							
					</ul>
					
						<!-- 	</div>-->
					

				</li>
				
			
				
			</c:forEach>
		</ul>

	</c:if>


</body>
</html>