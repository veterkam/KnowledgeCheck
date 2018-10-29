<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="Header.jsp" %>
<%@ include file = "NavBar.jsp" %>
<div class="container">
    <h1 class="h3 mb-3 font-weight-normal">Registration</h1>
    <%@ include file = "ErrorMessage.jsp" %>
    <%@ include file = "AttentionMessage.jsp" %>
    <form class="form-register text-left" method="post">
        <div class="form-group">
            <label for="inputFirstName">First Name:</label>
            <input id="inputFirstName" class="form-control" name="firstname"
                   placeholder="Enter your first name" required="" autofocus="" type="text"
                   value="<c:out value="${anonym.firstname}"/>"
                   <c:if test="${verifyEmail != null}">disabled</c:if>>
        </div>
        <div class="form-group">
            <label for="inputLastName" >Last Name:</label>
            <input id="inputLastName" class="form-control" name="lastname"
                   placeholder="Enter your last name" required="" autofocus="" type="text"
                   value="<c:out value="${anonym.lastname}"/>"
                   <c:if test="${verifyEmail != null}">disabled</c:if>>
        </div>
        <div class="form-group">
            <label for="inputEmail" >E-mail:</label>
            <input id="inputEmail" class="form-control" name="email"
                   placeholder="Enter your real e-mail address" required="" autofocus="" type="email"
                   value="<c:out value="${anonym.email}"/>"
                   <c:if test="${verifyEmail != null}">disabled</c:if>>
        </div>
        <div class="form-group">
            <label for="inputUsername" >Username:</label>
            <input id="inputUsername" class="form-control" name="username"
                   placeholder="Enter your username" required="" autofocus="" type="username"
                   value="<c:out value="${anonym.username}"/>"
                   <c:if test="${verifyEmail != null}">disabled</c:if>>
        </div>
        <c:if test="${verifyEmail == null}">
            <div class="form-group">
                <label for="inputPassword" >Password:</label>
                <input id="inputPassword" class="form-control" name="password"
                       placeholder="Enter your password" required="" type="password"
                       value="<c:out value="${anonym.password}"/>">
            </div>
            <div class="form-group">
                <label for="inputRepeatPassword" class="sr-only">Password:</label>
                <input id="inputRepeatPassword" class="form-control" name="repeatPassword"
                       placeholder="Repeat your password" required="" type="password">
            </div>
        </c:if>
        <div class="form-group">
            <label for="selectRole" >Role:</label>
            <select id="selectRole" name="role" class="custom-select mb-3"
                    <c:if test="${verifyEmail != null}">disabled</c:if>>
                <option disabled>Choose a role</option>
                <c:forEach var="role" items="${roles}">
                    <option <c:if test="${role == anonym.role}">selected</c:if>  value="${role}">
                        <c:out value="${role}"/>
                    </option>
                </c:forEach>
            </select>
        </div>
        <c:if test="${verifyEmail != null}">
            <div class="form-group">
                <label for="inputVerificationCode" >Verification code:</label>
                <input id="inputVerificationCode" class="form-control" name="verificationCode"
                       placeholder="Enter verification code" required="" autofocus="" type="text">
            </div>
        </c:if>

        <c:if test="${verifyEmail != null}">
            <div class="form-group text-center">
                <button class="btn btn-dark" type="submit" name="btnCancel" formnovalidate
                        formaction="${pageContext.request.contextPath}/authorization/register">
                    Cancel
                </button>
                <button class="btn btn-dark" type="submit" name="btnBack" formnovalidate
                        formaction="${pageContext.request.contextPath}/authorization/register/back">
                    Back
                </button>
                <button class="btn btn-dark" type="submit" name="btnRegister"
                        formaction="${pageContext.request.contextPath}/authorization/register/confirm">
                    Register
                </button>
            </div>
        </c:if>
        <c:if test="${verifyEmail == null}">
            <button class="btn btn-dark btn-block" type="submit" name="btnNext"
                    formaction="${pageContext.request.contextPath}/authorization/register/verify">
                Next
            </button>
        </c:if>
        <p class="text-center mt-5">
            Do you have an account?
            <a href="${pageContext.request.contextPath}/authorization/login">Login</a>
        </p>
    </form>
</div>
<%@ include file="Footer.jsp" %>
