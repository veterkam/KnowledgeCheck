<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- navbar -->
<nav class="navbar navbar-expand-sm fixed-top bg-dark navbar-dark">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/">Home</a>
    <c:if test="${user != null}">
        <c:if test="${user.role=='TUTOR'}">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/profile">My Profile</a>
            <a class="navbar-brand" href="${pageContext.request.contextPath}/testboard/mytests">My Tests</a>
        </c:if>
        <c:if test="${user.role=='TUTOR' || user.role=='STUDENT'}">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/rating">Student Rating</a>
        </c:if>
    </c:if>
    <!-- Toggler/collapsibe Button -->
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsibleNavbar">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse text-right" id="collapsibleNavbar">
        <c:if test="${user != null}">
            <span class="navbar-text">Welcome <c:out value="${user.username}"/>!</span>
        </c:if>
        <ul class="navbar-nav">
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

