<%@ include file="../common/Header.jsp" %>
    <h1 class="h3 mb-3 font-weight-normal"><fmt:message key="app.account.please_login" /></h1>

    <form class="form-login text-left" method="post"
            action="${pageContext.request.contextPath}/account/login"
          name="loginForm">
        <div class="form-row">
            <%@ include file = "../common/Alert.jsp" %>
        </div>
        <div class="form-group">
            <label for="inputUsername" class="sr-only">Username</label>
            <input id="inputUsername" class="form-control" name="username" placeholder="Your Username" required="" autofocus="" type="username">
        </div>
        <div class="form-group">
            <label for="inputPassword" class="sr-only">Password</label>
            <input id="inputPassword" class="form-control" name="password" placeholder="Your Password" required="" type="password">
        </div>
        <div class="form-row">
            <button class="btn btn-dark btn-block" type="submit">Login</button>
        </div>

        <div class="form-row">
            <small>
                You don't have an account?
                <a href="${pageContext.request.contextPath}/account/register">
                    Register</a><br/>
                You don't remember a password?
                <a href="${pageContext.request.contextPath}/account/recovery">
                    Recovery</a>
            </small>
        </div>
    </form>
<%@ include file="../common/JSlibs.jsp" %>
<%@ include file="../common/Footer.jsp" %>
