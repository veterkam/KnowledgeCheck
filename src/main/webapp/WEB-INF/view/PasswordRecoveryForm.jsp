<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="Header.jsp" %>
<%@ include file = "NavBar.jsp" %>
<div align="center">
    <h2>Password recovery</h2>
    <%@ include file = "ErrorMessage.jsp" %>
    <%@ include file = "AttentionMessage.jsp" %>
    <form method="post" name="recoveryForm" onsubmit="return validate();">
        <table>
            <tr>
                <td>Username:</td>
                <td>
                    <input type="text" name="username" value="<c:out value="${anonym.username}"/>"
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
            <c:if test="${verifyEmail == null}">
                <tr>
                    <td>New Password:</td>
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
            <c:if test="${verifyEmail != null}">
                <tr>
                    <td>E-mail verification code:</td>
                    <td><input type="text" name="verificationCode"></td>
                </tr>
            </c:if>
        </table><br>
        <c:if test="${verifyEmail != null}">
            <input type="submit" name="btnCancel" value="Cancel"
                   formaction="${pageContext.request.contextPath}/authorization/recovery">
            <input type="submit" name="btnBack" value="Back"
                   formaction="${pageContext.request.contextPath}/authorization/recovery/back">
            <input type="submit" name="btnRecovery" value="Recovery"
                   formaction="${pageContext.request.contextPath}/authorization/recovery/confirm">
        </c:if>
        <c:if test="${verifyEmail == null}">
            <input type="submit" name="btnNext" value="Next"
                   formaction="${pageContext.request.contextPath}/authorization/recovery/verify">
        </c:if>

    </form>
</div>
<%@ include file="Footer.jsp" %>
