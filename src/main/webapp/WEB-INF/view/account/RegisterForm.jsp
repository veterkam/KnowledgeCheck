<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="../common/Header.jsp" %>
<%@ include file = "../common/NavBar.jsp" %>
<div class="container">
    <h1 class="h3 mb-3 font-weight-normal">Register</h1>

    <form class="form-register text-left col-12">
        <%@ include file = "../common/Alert.jsp" %>
        <div class="form-row">
            <label class="col-sm-4 col-form-label" for="inputFirstName">First Name:</label>
            <input id="inputFirstName" class="form-control col-sm-8 mb-1"
                   placeholder="Enter your first name" required="" autofocus="" type="text"
                   name="firstname" value="<c:out value="${anonym.firstname}"/>"
                   <c:if test="${verifyEmail != null}">disabled</c:if>>
        </div>
        <div class="form-row">
            <label class="col-sm-4 col-form-label" for="inputLastName" >Last Name:</label>
            <input id="inputLastName" class="form-control col-sm-8 mb-1"
                   placeholder="Enter your last name" required="" autofocus="" type="text"
                   name="lastname" value="<c:out value="${anonym.lastname}"/>"
                   <c:if test="${verifyEmail != null}">disabled</c:if>>
        </div>
        <div class="form-row">
            <label class="col-sm-4 col-form-label" for="inputUsername" >Username:</label>
            <input id="inputUsername" class="form-control col-sm-8 mb-1"
                   placeholder="Enter your username" required="" autofocus="" type="username"
                   name="username" value="<c:out value="${anonym.username}"/>"
                   <c:if test="${verifyEmail != null}">disabled</c:if>>
        </div>
        <div class="form-row">
            <label class="col-sm-4 col-form-label" for="inputEmail" >E-mail:</label>
            <input id="inputEmail" class="form-control col-sm-8 mb-1" name="email"
                   placeholder="Enter your real e-mail address" required="" autofocus="" type="email"
                   value="<c:out value="${anonym.email}"/>"
                   <c:if test="${verifyEmail != null}">disabled</c:if>>
        </div>
        <c:if test="${verifyEmail == null}">
            <div class="form-row">
                <label class="col-sm-4 col-form-label" for="inputPassword" >Password:</label>
                <input id="inputPassword" class="form-control col-sm-8 mb-1" name="password"
                       placeholder="Enter your password" required="" type="password">
            </div>
            <div class="form-row">
                <label class="col-sm-4 col-form-label" for="inputRepeatPassword" class="sr-only">Repeat Password:</label>
                <input id="inputRepeatPassword" class="form-control col-sm-8 mb-1" name="repeatPassword"
                       placeholder="Repeat your password" required="" type="password">
            </div>
        </c:if>
        <div class="form-row">
            <label class="col-sm-4 col-form-label" for="selectRole" >Role:</label>
            <select id="selectRole" name="role" class="custom-select form-control col-sm-8 mb-1"
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
            <div class="form-row">
                <label class="col-sm-4 col-form-label" for="inputVerificationCode" >Verification code:</label>
                <input id="inputVerificationCode" class="form-control col-sm-8 mb-1" name="verificationCode"
                       placeholder="Enter verification code" required="" autofocus="" type="text">
            </div>
        </c:if>

        <div class="row">
        <c:if test="${verifyEmail != null}">
            <div class="col-4">
                <a class="btn btn-dark btn-block m-1" role="button"
                   href="${pageContext.request.contextPath}/account/register" >
                    Cancel
                </a>
            </div>
            <div class="col-4">
                <a class="btn btn-dark btn-block m-1" role="button"
                   href="${pageContext.request.contextPath}/account/register?back">
                    Back
                </a>
            </div>
            <div class="col-4">
                <button class="btn btn-dark btn-block m-1" type="submit" name="btnRegister"
                        formaction="${pageContext.request.contextPath}/account/register/confirm" formmethod="post">
                    Register
                </button>
            </div>
        </c:if>
        <c:if test="${verifyEmail == null}">
            <div class="col-3"></div>
            <div class="col-6">
                <button class="btn btn-dark btn-block m-1" type="submit" name="btnNext"
                        formaction="${pageContext.request.contextPath}/account/register" formmethod="post">
                    Next
                </button>
            </div>
            <div class="col-3"></div>
        </c:if>
        </div>
        <div class="row">
            <div class="col text-center">
                <p class="mt-5">
                    Do you have an account?
                    <a href="${pageContext.request.contextPath}/account/login">Login</a>
                </p>
            </div>
        </div>
    </form>
</div>
<%@ include file="../common/JSlibs.jsp" %>
<%@ include file="../common/Footer.jsp" %>
