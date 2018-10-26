<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="Head.jsp" %>
<%@ include file = "Menu.jsp" %>
<div align="center">
    <h2>Registration</h2>
    <%@ include file = "ErrorMessage.jsp" %>
    <form method="post" name="registerForm" onsubmit="return validate();">
        <table>
            <tr>
                <td>First Name:</td>
                <td>
                    <input type="text" name="firstname" value="<c:out value="${anonym.firstname}"/>"
                        <c:if test="${verifyEmail != null}">disabled</c:if>>
                </td>
            </tr>
            <tr>
                <td>Last Name:</td>
                <td>
                    <input type="text" name="lastname" value="<c:out value="${anonym.lastname}"/>"
                           <c:if test="${verifyEmail != null}">disabled</c:if>>
                </td>
            </tr>
            <tr>
                <td>E-mail:</td>
                <td>
                    <input type="text" name="email" value="<c:out value="${anonym.email}"/>"
                           <c:if test="${verifyEmail != null}">disabled</c:if>>
                </td>
            </tr>
            <tr>
                <td>Username:</td>
                <td>
                    <input type="text" name="username" value="<c:out value="${anonym.username}"/>"
                           <c:if test="${verifyEmail != null}">disabled</c:if>>
                </td>
            </tr>
            <c:if test="${verifyEmail == null}">
                <tr>
                    <td>Password:</td>
                    <td>
                        <input type="password" name="password" value="<c:out value="${anonym.password}"/>">
                    </td>
                </tr>
                <tr>
                    <td>Repeat Password:</td>
                    <td>
                        <input type="password" name="repeatPassword" value="">
                    </td>
                </tr>
            </c:if>
            <tr>
                <td>Role:</td>
                <td>
                    <select name="role" <c:if test="${verifyEmail != null}">disabled</c:if> >
                        <option disabled>Choose a role</option>
                        <c:forEach var="role" items="${roles}">
                            <option <c:if test="${role == anonym.role}">selected</c:if>  value="${role}">
                                <c:out value="${role}"/>
                            </option>
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
            <input type="submit" name="btnCancel" value="Cancel"
                   formaction="${pageContext.request.contextPath}/authorization/register">
            <input type="submit" name="btnBack" value="Back"
                   formaction="${pageContext.request.contextPath}/authorization/register/back">
            <input type="submit" name="btnRegister" value="Register"
                   formaction="${pageContext.request.contextPath}/authorization/register/confirm">
        </c:if>
        <c:if test="${verifyEmail == null}">
            <input type="submit" name="btnNext" value="Next"
                   formaction="${pageContext.request.contextPath}/authorization/register/verify">
        </c:if>
        <h3>Do you have an account? <a href="${pageContext.request.contextPath}/authorization/login">Login</a></h3>

    </form>
</div>
</body>
</html>
