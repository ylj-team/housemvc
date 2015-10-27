<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>   
<%@ page isELIgnored="false"%>



<html>
<head>
<title>Hello </title>
</head>
<body>
   <h2>user<c:out value="=>${user}"/></h2>
   <h4>user<c:out value="=>${message}"/></h4>
</body>
</html>