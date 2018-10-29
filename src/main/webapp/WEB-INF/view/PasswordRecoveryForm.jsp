<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="Header.jsp" %>
<%@ include file = "NavBar.jsp" %>
<div class="container">
    <h1 class="h3 mb-3 font-weight-normal">Password recovery</h1>
    <%@ include file = "Alert.jsp" %>
    <form class="form-register text-left col-12" method="post">
        <div class="form-row">
            <label class="col-sm-4 col-form-label" for="inputUsername" >Username:</label>
            <input id="inputUsername" class="form-control col-sm-8 mb-1" name="username"
                   placeholder="Enter your username" required="" autofocus="" type="username"
                   value="<c:out value="${anonym.username}"/>"
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
                       placeholder="Enter your password" required="" type="password"
                       value="<c:out value="${anonym.password}"/>">
            </div>
            <div class="form-row">
                <label class="col-sm-4 col-form-label" for="inputRepeatPassword" class="sr-only">Repeat Password:</label>
                <input id="inputRepeatPassword" class="form-control col-sm-8 mb-1" name="repeatPassword"
                       placeholder="Repeat your password" required="" type="password">
            </div>
        </c:if>
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
                    <button class="btn btn-dark btn-block m-1" type="submit" name="btnCancel" formnovalidate
                            formaction="${pageContext.request.contextPath}/authorization/recovery">
                        Cancel
                    </button>
                </div>
                <div class="col-4">
                    <button class="btn btn-dark btn-block m-1" type="submit" name="btnBack" formnovalidate
                            formaction="${pageContext.request.contextPath}/authorization/recovery/back">
                        Back
                    </button>
                </div>
                <div class="col-4">
                    <button class="btn btn-dark btn-block m-1" type="submit" name="btnRecovery"
                            formaction="${pageContext.request.contextPath}/authorization/recovery/confirm">
                        Recovery
                    </button>
                </div>
            </c:if>
            <c:if test="${verifyEmail == null}">
                <div class="col-3"></div>
                <div class="col-6">
                    <button class="btn btn-dark btn-block m-1" type="submit" name="btnNext"
                            formaction="${pageContext.request.contextPath}/authorization/recovery/verify">
                        Next
                    </button>
                </div>
                <div class="col-3"></div>
            </c:if>
        </div>
    </form>
</div>
<%@ include file="Footer.jsp" %>
