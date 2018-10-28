<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="Header.jsp" %>
<%@ include file = "Menu.jsp" %>
    <div align="center">
        <h2>Welcome <c:out value="${user.username}"/> / <c:out value="${user.role}"/>!</h2>
    </div>
<%@ include file="Footer.jsp" %>
