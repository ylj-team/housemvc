<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>


<html>
<head>
<title>用户首页</title>

</head>
<body>
	<h3>
		<a href="./userIdx" ><c:out value="=>${account}" /></a>	
		<a href="./loginpasswordchange.html?account=${account}" >修改密码</a>
		<a href="./do_logout" >logout</a>
	</h3>
	
	
	<h2>
		<c:out value="-----------------------------------" />
	</h2>	
	<p>
		<a href="./daily_jstl" >今日签约（全部）</a>
		<a href="./daily_jstl?city=hz" > 今日签约（主城区）</a>
		<a href="./daily_jstl?city=xs" > 今日签约（萧山）</a>
		<a href="./daily_jstl?city=yh" > 今日签约（余杭）</a>
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
		<c:out value="---------------已关注楼盘------------------" />	
	</h2>
	
		<c:forEach var="subscription" items="${subscriptions}">
				
			
				<li>
	
					<a href="./property_jstl?propertyId=${subscription.propertyId}" target="_blank">${subscription.propertyName}(${subscription.signNumber})</a>

				</li>
				
			
				
			</c:forEach>
	
	
	<h2>
		<c:out value="--------------------------------------" />	
	</h2>
	
</body>
</html>