<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- navbar -->
<nav class="navbar navbar-expand-sm fixed-top bg-dark navbar-dark">
    <a class="navbar-brand" href="<c:url value="/" />">Home</a>
    <c:if test="${user != null}">
        <a class="navbar-brand" href="<c:url value="/account/myprofile" />">My Profile</a>

        <c:if test="${user.role=='ADMINISTRATOR'}">
            <a class="navbar-brand" href="<c:url value="/account/users" />" >Users</a>
        </c:if>

        <c:if test="${user.role=='TUTOR'}">
            <a class="navbar-brand" href="<c:url value="/testboard/mytests"/>">My Tests</a>
            <a class="navbar-brand" href="<c:url value="/testboard/studentsresults"/>">Students Results</a>
            <a class="navbar-brand" href="<c:url value="/testboard/teststatistics"/>">Test Statistics</a>
            <a class="navbar-brand" href="<c:url value="/testboard/subjects"/>">Subjects</a>
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
                       href="<c:url value="/account/logout"/>">
                        Logout
                    </a>
                </li>
            </c:if>
            <c:if test="${user == null}">
                <li class="nav-item">
                    <a class="nav-link" data-value="login"
                        href="<c:url value="/account/login" />">
                        Login
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-value="register"
                       href="<c:url value="/account/register"/>">
                        Register
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-value="recovery"
                       href="<c:url value="/account/recovery"/>">
                        Password recovery
                    </a>
                </li>
            </c:if>
        </ul>
    </div>
</nav>

