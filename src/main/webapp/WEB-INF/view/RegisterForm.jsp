<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="Head.jsp" %>
<%@ include file = "Menu.jsp" %>
<div align="center">
    <h2>Registration</h2>
    <%@ include file = "ErrorMessage.jsp" %>
    <form method="get" action="${pageContext.request.contextPath}/authorization/register" name="registerForm" onsubmit="return validate();">
        <table>
            <tr>
                <td>First Name:</td>
                <td>
                    <input type="text" name="firstname" value="${param.firstname}"
                        <c:if test="${verifyEmail != null}">disabled</c:if>>
                </td>
            </tr>
            <tr>
                <td>Last Name:</td>
                <td>
                    <input type="text" name="lastname" value="${param.lastname}"
                           <c:if test="${verifyEmail != null}">disabled</c:if>>
                </td>
            </tr>
            <tr>
                <td>E-mail:</td>
                <td>
                    <input type="text" name="email" value="${param.email}"
                           <c:if test="${verifyEmail != null}">disabled</c:if>>
                </td>
            </tr>
            <tr>
                <td>Username:</td>
                <td>
                    <input type="text" name="username" value="${param.username}"
                           <c:if test="${verifyEmail != null}">disabled</c:if>>
                </td>
            </tr>
            <tr>
                <td>Password:</td>
                <td>
                    <input type="password" name="password" value="${param.password}"
                           <c:if test="${verifyEmail != null}">disabled</c:if>>
                </td>
            </tr>
            <tr>
                <td>Repeat Password:</td>
                <td>
                    <input type="password" name="repeatPassword" value="${param.repeatPassword}"
                           <c:if test="${verifyEmail != null}">disabled</c:if>>
                </td>
            </tr>
            <tr>
                <td>Role:</td>
                <td>
                    <select name="role" <c:if test="${verifyEmail != null}">disabled</c:if> >
                        <option disabled>Choose a role</option>
                        <c:forEach var="role" items="${roles}">
                            <option value="${role}"><c:out value="${role}"/></option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <c:if test="${verifyEmail != null}">
                <tr>
                    <td>E-mail verification code:</td>
                    <td><input type="text" name="verificationCode"></td>
                </tr>
            </c:if>
        </table><br>
        <c:if test="${verifyEmail != null}">
            <input type="submit" name="action" value="Cancel">
            <input type="submit" name="action" value="Back">
            <input type="submit" name="action" value="Register">
        </c:if>
        <c:if test="${verifyEmail == null}">
            <input type="submit" name="action" value="Next">
        </c:if>
        <h3>Do you have an account? <a href="${pageContext.request.contextPath}/authorization/login">Login</a></h3>

    </form>
</div>
</body>
</html>
