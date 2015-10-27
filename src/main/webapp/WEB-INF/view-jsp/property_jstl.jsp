<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>


<html>
<head>
<title>StateChangedHouse_两个DIV并排</title>


<style>
.div-a {
	float: left;
	width: 49%;
	border: 1px solid #000
}

.div-b {
	float: left;
	width: 49%;
	border: 1px solid #000
}
</style>

</head>
<body>
	<h2>
		<a href="./user" ><c:out value="=>${account}" /></a>
		<a href="./do_logout" >logout</a>
	
	</h2>

	
	
	<form name="houseSaleFind" action="./property_jstl" method="get" >
	
			楼盘：<input type="text" name="propertyId" value="${propertyId}" />
			开始日期：<input type="date" name="dateFrom" value="${dateFrom}"/>
			截止日期：<input type="date" name="dateTo" value="${dateTo}"/>
			
			<input type="submit" value="查询" />
			
	</form>

	
	
	
	<c:if test="${not empty dailyStates}">

		<ul>
			<c:forEach var="dailyState" items="${dailyStates}">
				
				<HR align=center width=100% color=#987cb9 SIZE=1>
				
				<li>
					<h3>
						<c:out value="--------  ${dailyState.date} ----------" />
					</h3> 
					
					<div class="div-a">
					
						<c:forEach var="signed" items="${dailyState.sharpedSigneds}">
							<ul>
								<li>签约套数：${signed.signedNumber}</li>
								<li>签约面积：${signed.signedAreaFormat}</li>
								<li>签约均价：${signed.signedAvgPrice}</li>	
								<li>签约总价：${signed.signed_zongjia}(申报总价：${signed.declare_Total_dePrice_zongjia}) 折扣：${signed.saledDiscount}</li>											
								<li>${signed.signedTime}</li>
								
							</ul>
							<br>
						</c:forEach> 				
					</div>
					
				<!-- 	<c:out value="--------------------------------" />   -->
					<div class="div-b">
					
						<c:forEach var="stateInfo" items="${dailyState.stateInfos}">
							<ul>
								<li>${stateInfo.houseStateName}</li>
								<li>${stateInfo.located}</li>
								<li>${stateInfo.area_builtup}(${stateInfo.area_inside})得房率：${stateInfo.area_Rate}</li>			
								<li>申报总价：${stateInfo.dePrice_zongjia}</li>
								<li>${stateInfo.stateChangeTime}</li>
							</ul>
							<br>
						</c:forEach>
					
					</div>

				</li>
				
			
				
			</c:forEach>
		</ul>

	</c:if>


</body>
</html>