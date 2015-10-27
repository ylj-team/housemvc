

<%@ page contentType="text/html; charset=UTF-8"%>

<%! int fontSize; %> 

<html>
<head>
<title>Hello</title>
</head>
<body>
	<h2><%=request.getAttribute("dailySigneds.size")%></h2>
	<h4><%=request.getAttribute("message")%></h4>

	<%--
		List<HouseState> states = request.getAttribute("states");
		for (HouseState state : states) {

		}
	--%>
	<%
		for (fontSize = 1; fontSize <= 3; fontSize++) {
	%>
	<font color="green" size="<%=fontSize%>"> JSP Tutorial </font>
	<br />
	<%
		}
	%>


	<ul>
		<li>Coffee</li>
		<li>Milk</li>
	</ul>
</body>
</html>