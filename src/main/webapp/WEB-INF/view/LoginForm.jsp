<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="Header.jsp" %>
<%@ include file="Menu.jsp" %>


    <form class="form-login" method="post" action="${pageContext.request.contextPath}/authorization/login" name="loginForm">
        <h1 class="h3 mb-3 font-weight-normal">Please login</h1>
        <%@ include file="ErrorMessage.jsp" %>
        <label for="inputUsername" class="sr-only">Username</label>
        <input id="inputUsername" class="form-control" name="username" placeholder="Username" required="" autofocus="" type="username">
        <label for="inputPassword" class="sr-only">Password</label>
        <input id="inputPassword" class="form-control" name="password" placeholder="Password" required="" type="password">

        <button class="btn btn-lg btn-dark btn-block" type="submit">Login</button>

        <p>
            You don't have an account?
            <a href="${pageContext.request.contextPath}/authorization/register">Register</a>
        </p>
        <p>
            You don't remember a password?
            <a href="${pageContext.request.contextPath}/authorization/recovery">Recovery</a>
        </p>
    </form>

<%@ include file="Footer.jsp" %>
