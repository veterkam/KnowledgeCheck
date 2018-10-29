<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="Header.jsp" %>
<%@ include file = "NavBar.jsp" %>
<div class="container">
    <h1 class="h3 mb-3 font-weight-normal">Welcome</h1>
    <%@ include file = "Alert.jsp" %>
    <h2>Welcome <c:out value="${user.username}"/> / <c:out value="${user.role}"/>!</h2>
</div>
<%@ include file="Footer.jsp" %>
