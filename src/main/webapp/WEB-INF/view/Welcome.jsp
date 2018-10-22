<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Login & Register</title>
</head>
<body>
    <%@ include file = "Menu.jsp" %>
    <div align="center">
        <h2>Welcome <c:out value="${user.username}"/>!</h2>
    </div>
</body>
</html>
