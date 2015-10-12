<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
<title>Hello </title>
</head>
<body>
   <h2><%=request.getAttribute("user") %></h2>
   <h4><%=request.getAttribute("message") %></h4>
</body>
</html>