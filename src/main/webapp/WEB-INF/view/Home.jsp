<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file = "Head.jsp" %>
<%@ include file = "Menu.jsp" %>
<div align="center">
    <h2>
        Welcome to Home page
        <c:if test="${user != null}">
            <c:out value="${user.username}"/> / <c:out value="${user.role}"/>
        </c:if>
        !
    </h2>
</div>
</body>
</html>
