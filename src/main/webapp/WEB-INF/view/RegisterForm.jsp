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
    <h2>Register</h2>
    <c:if test="${registerFailedMsg != null}">
        <h3><c:out value="${registerFailedMsg}"/></h3>
    </c:if>
    <form method="post" action="register" name="registerForm" onsubmit="return validate();">
        <table>
            <tr>
                <td>First Name:</td>
                <td><input type="text" name="firstname"></td>
            </tr>
            <tr>
                <td>Last Name:</td>
                <td><input type="text" name="lastname"></td>
            </tr>
            <tr>
                <td>Username:</td>
                <td><input type="text" name="username"></td>
            </tr>
            <tr>
                <td>Password:</td>
                <td><input type="password" name="password"></td>
            </tr>
        </table><br>
        <input type="submit" name="registerButton" value="Register">
        <h3>Do you have an account? <a href="login">Login</a></h3>

    </form>
</div>

</body>
</html>
