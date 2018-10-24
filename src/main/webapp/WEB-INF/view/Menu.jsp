<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div align="right" style="background-color: orangered">
    <c:if test="${user != null}">
        <span>Welcome <c:out value="${user.username}"/>!</span>
        <span><a href="${pageContext.request.contextPath}/authorization/sendmail">Send e-mail</a></span>
        <span><a href="${pageContext.request.contextPath}/authorization/logout">Logout</a></span>
    </c:if>
    <c:if test="${user == null}">
        <span><a href="${pageContext.request.contextPath}/authorization/login">Login</a></span> or
        <span><a href="${pageContext.request.contextPath}/authorization/register">Register</a></span>
    </c:if>
</div>
