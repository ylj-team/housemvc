<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>


<html>
<head>
<title>DailySignedRecords</title>
</head>
<body>
	<h2>
		<c:out value="=>${account}" />
		<a href="./do_logout" >logout</a>
	</h2>
	


	<c:if test="${not empty propertyRecords}">

		<ul>
			<c:forEach var="propertyRecord" items="${propertyRecords}">
				<li>
					<ul>
						<li>${propertyRecord.propertyName}</li>
						<li>${propertyRecord.propertyId}</li>
						<li><a href="${propertyRecord.url}" target="_blank">StateChanged</a></li>
					
						<br>
						<c:forEach var="signedRecord"
							items="${propertyRecord.signedRecords}">
							<c:out value="${signedRecord.signedNumber}"></c:out>
							<!--  		<c:out value="${signedRecord.reservedNumber}"></c:out> -->
							<c:out value="${signedRecord.signedArea}"></c:out>
							<c:out value="${signedRecord.signedAvgPrice}"></c:out>
							<c:out value="${signedRecord.signedTime}"></c:out>
							<br>
						</c:forEach>
					</ul>


				</li>

			</c:forEach>
		</ul>

	</c:if>


</body>
</html>