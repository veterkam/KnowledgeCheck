<%@ include file="../common/Header.jsp" %>
    <h1 class="h3 mb-3 font-weight-normal text-center"><fmt:message key="app.account.please_login" /></h1>

    <form class="form-login text-left" method="post"
            action="${pageContext.request.contextPath}/account/login"
          name="loginForm">
        <div class="form-row">
            <%@ include file = "../common/Alert.jsp" %>
        </div>
        <div class="form-group">
            <label for="inputUsername" class="sr-only"><fmt:message key="app.account.username" /></label>
            <input id="inputUsername" class="form-control" name="username" placeholder="<fmt:message key="app.account.enter_your_username" />" required="" autofocus="" type="username">
        </div>
        <div class="form-group">
            <label for="inputPassword" class="sr-only"><fmt:message key="app.account.password" /></label>
            <input id="inputPassword" class="form-control" name="password" placeholder="<fmt:message key="app.account.enter_your_password" />" required="" type="password">
        </div>
        <div class="form-row">
            <input hidden name="_FID" type="text" value="${FID}">
        </div>
        <div class="form-row">
            <button class="btn btn-dark btn-block" type="submit"><fmt:message key="app.account.login" /></button>
        </div>

        <div class="form-row">
            <small>
                <fmt:message key="app.account.you_do_not_have_an_account" />
                <a href="${pageContext.request.contextPath}/account/registration">
                    <fmt:message key="app.account.registration" /></a><br/>
                <fmt:message key="app.account.you_do_not_remember_a_password" />
                <a href="${pageContext.request.contextPath}/account/recovery">
                    <fmt:message key="app.account.password_recovery" /></a>
            </small>
        </div>
    </form>
<%@ include file="../common/JSlibs.jsp" %>
<%@ include file="../common/Footer.jsp" %>
