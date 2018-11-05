<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file = "Header.jsp" %>
<%@ include file = "NavBar.jsp" %>
<div class="container">
    <h1 class="h3 mb-3 font-weight-normal">Sorry, page not found!</h1>
    <%@ include file = "Alert.jsp" %>
    <p class="text-center mt-5">
        Please, start with Home Page.
        <a href="${pageContext.request.contextPath}/">Go Home</a>
    </p>
</div>
<%@ include file="Footer.jsp" %>
