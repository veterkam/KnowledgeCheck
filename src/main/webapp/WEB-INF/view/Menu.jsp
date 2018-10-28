<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- navbar -->
<nav class="navbar navbar-expand-lg fixed-top bg-dark">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/">Home</a>
    <c:if test="${user != null}">
        <c:if test="${user.role=='TUTOR' || user.role=='STUDENT'}">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/profile">My Profile</a>
            <a class="navbar-brand" href="${pageContext.request.contextPath}/rating">Student Rating</a>
        </c:if>
    </c:if>

    <div class="collapse navbar-collapse " id="navbarSupportedContent">
        <c:if test="${user != null}">
            <span style="color: whitesmoke">Welcome <c:out value="${user.username}"/>!</span>
        </c:if>
        <ul class="navbar-nav mr-4">
            <c:if test="${user != null}">
                <li class="nav-item">
                    <a class="nav-link" data-value="logout"
                       href="${pageContext.request.contextPath}/authorization/logout">
                        Logout
                    </a>
                </li>
            </c:if>
            <c:if test="${user == null}">
                <li class="nav-item">
                    <a class="nav-link" data-value="login"
                        href="${pageContext.request.contextPath}/authorization/login">
                        Login
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-value="register"
                       href="${pageContext.request.contextPath}/authorization/register">
                        Register
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-value="recovery"
                       href="${pageContext.request.contextPath}/authorization/recovery">
                        Password recovery
                    </a>
                </li>
            </c:if>
        </ul>
    </div>
</nav>

