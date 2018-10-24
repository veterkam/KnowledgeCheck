<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="Head.jsp" %>
<%@ include file="Menu.jsp" %>
<div align="center">
    <h2>Login</h2>
    <%@ include file = "ErrorMessage.jsp" %>
    <form method="post" action="${pageContext.request.contextPath}/authorization/login" name="loginForm" onsubmit="return validate();">
        <table>
            <tr>
                <td>Username:</td>
                <td><input type="text" name="username"></td>
            </tr>
            <tr>
                <td>Password:</td>
                <td><input type="password" name="password"></td>
            </tr>
        </table><br>

        <input type="submit" name="loginButton" value="Login">
        <h3>You don't have an account? <a href="${pageContext.request.contextPath}/authorization/register">Register</a></h3>

    </form>
</div>

</body>
</html>
