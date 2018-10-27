<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="top-menu">

    <div class="left">
        <span><a href="${pageContext.request.contextPath}/">Home</a></span>
        <%--<span><a href="${pageContext.request.contextPath}/testboard">Tests</a></span>--%>
        <c:if test="${user != null}">
            <c:if test="${user.role=='TUTOR' || user.role=='STUDENT'}">
              /  <span><a href="${pageContext.request.contextPath}/profile">My Profile</a></span>
              /  <span><a href="${pageContext.request.contextPath}/rating">Student Rating</a></span>
            </c:if>
        </c:if>
    </div>

    <div class="right">
        <c:if test="${user != null}">
            <span>Welcome <c:out value="${user.username}"/>!</span>
            <span><a href="${pageContext.request.contextPath}/authorization/logout">Logout</a></span>
        </c:if>
        <c:if test="${user == null}">
            <span><a href="${pageContext.request.contextPath}/authorization/login">Login</a></span> /
            <span><a href="${pageContext.request.contextPath}/authorization/register">Register</a></span> /
            <span><a href="${pageContext.request.contextPath}/authorization/recovery">Password recovery</a></span>
        </c:if>
    </div>

</div>
