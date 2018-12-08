<!-- navbar -->
<nav class="navbar navbar-expand-sm fixed-top bg-dark navbar-dark">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/"><fmt:message key="app.menu.home" /></a>
    <c:if test="${user != null}">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/account/myprofile"><fmt:message key="app.menu.my_profile" /></a>

        <c:if test="${user.role=='ADMINISTRATOR'}">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/account/users" ><fmt:message key="app.menu.users" /></a>
        </c:if>

        <c:if test="${user.role=='TUTOR'}">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/testing/mytests"><fmt:message key="app.menu.my_tests" /></a>
            <a class="navbar-brand" href="${pageContext.request.contextPath}/testing/studentsresults"><fmt:message key="app.menu.students_results" /></a>
            <a class="navbar-brand" href="${pageContext.request.contextPath}/testing/teststatistics"><fmt:message key="app.menu.test_statistics" /></a>
            <a class="navbar-brand" href="${pageContext.request.contextPath}/testing/subjects"><fmt:message key="app.menu.subjects" /></a>
        </c:if>
    </c:if>
    <!-- Toggler/collapsibe Button -->
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsibleNavbar">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse text-right" id="collapsibleNavbar">
        <c:if test="${user != null}">
            <span class="navbar-text"><fmt:message key="app.common.welcome" /> <c:out value="${user.username}"/>!</span>
        </c:if>
        <ul class="navbar-nav">
            <c:if test="${user != null}">
                <li class="nav-item">
                    <a class="nav-link" data-value="logout"
                       href="${pageContext.request.contextPath}/account/logout">
                        <fmt:message key="app.account.logout" />
                    </a>
                </li>
            </c:if>
            <c:if test="${user == null}">
                <li class="nav-item">
                    <a class="nav-link" data-value="login"
                        href="${pageContext.request.contextPath}/account/login">
                        <fmt:message key="app.account.login" />
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-value="registration"
                       href="${pageContext.request.contextPath}/account/registration">
                        <fmt:message key="app.account.registration" />
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-value="recovery"
                       href="${pageContext.request.contextPath}/account/recovery">
                        <fmt:message key="app.account.recovery" />
                    </a>
                </li>
            </c:if>
        </ul>
    </div>
</nav>

