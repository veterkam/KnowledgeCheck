<%@ include file="../common/Header.jsp" %>
    <h1 class="h3 mb-3 font-weight-normal text-center"><fmt:message key="app.account.password_recovery" /></h1>

    <form class="form-registration text-left col-12">
        <div class="form-group">
            <%@ include file = "../common/Alert.jsp" %>
        </div>
        <c:set value='${errors != null && errors.get("username") != null}' var="isInvalid" />
        <div class="form-row">
            <label class="col-sm-4 col-form-label" for="inputUsername" ><fmt:message key="app.account.username" /></label>
            <input id="inputUsername"
                   class="form-control col-sm-8 mb-1 ${(isInvalid) ? 'is-invalid' : ''}"
                   placeholder="<fmt:message key="app.account.enter_your_username" />" required="" autofocus="" type="username"
                   name="username" value="<c:out value="${userDto.username}"/>"
                   <c:if test="${verifyEmail != null}">disabled</c:if>>
        </div>
        <c:if test="${isInvalid}">
            <c:set value='${errors.get("username")}' var="fieldErrors" />
            <div class="form-group text-right">
                <c:forEach var="error" items="${fieldErrors}">
                    <small class="help-block text-danger text-right"><fmt:message key="${error}" /><br/></small>
                </c:forEach>
            </div>
        </c:if>

        <c:set value='${errors != null && errors.get("email") != null}' var="isInvalid" />
        <div class="form-row">
            <label class="col-sm-4 col-form-label" for="inputEmail" ><fmt:message key="app.account.email" /></label>
            <input id="inputEmail"
                   class="form-control col-sm-8 mb-1 ${(isInvalid) ? 'is-invalid' : ''}"
                   name="email"
                   placeholder="<fmt:message key="app.account.enter_your_real_email_address" />" required="" autofocus="" type="email"
                   value="<c:out value="${userDto.email}"/>"
                   <c:if test="${verifyEmail != null}">disabled</c:if>>
        </div>
        <c:if test="${isInvalid}">
            <c:set value='${errors.get("email")}' var="fieldErrors" />
            <div class="form-group text-right">
                <c:forEach var="error" items="${fieldErrors}">
                    <small class="help-block text-danger text-right"><fmt:message key="${error}" /><br/></small>
                </c:forEach>
            </div>
        </c:if>
        <c:if test="${verifyEmail == null}">
            <c:set value='${errors != null && errors.get("password") != null}' var="isInvalid" />
            <div class="form-row">
                <label class="col-sm-4 col-form-label" for="inputPassword" ><fmt:message key="app.account.password" /></label>
                <input id="inputPassword"
                       class="form-control col-sm-8 mb-1 ${(isInvalid) ? 'is-invalid' : ''}"
                       name="password"
                       value="<c:out value="${userDto.password}"/>"
                       placeholder="<fmt:message key="app.account.enter_your_password" />" required="" type="password">
            </div>
            <c:if test="${isInvalid}">
                <c:set value='${errors.get("password")}' var="fieldErrors" />
                <div class="form-group text-right">
                    <c:forEach var="error" items="${fieldErrors}">
                        <small class="help-block text-danger text-right"><fmt:message key="${error}" /><br/></small>
                    </c:forEach>
                </div>
            </c:if>

            <c:set value='${errors != null && errors.get("confirmPassword") != null}' var="isInvalid" />
            <div class="form-row">
                <label class="col-sm-4 col-form-label" for="inputConfirmPassword" class="sr-only"><fmt:message key="app.account.confirm_password" /></label>
                <input id="inputConfirmPassword"
                       class="form-control col-sm-8 mb-1 ${(isInvalid) ? 'is-invalid' : ''}"
                       name="confirmPassword"
                       value="<c:out value="${userDto.confirmPassword}"/>"
                       placeholder="<fmt:message key="app.account.confirm_your_password" />" required="" type="password">
            </div>
            <c:if test="${isInvalid}">
                <c:set value='${errors.get("confirmPassword")}' var="fieldErrors" />
                <div class="form-group text-right">
                    <c:forEach var="error" items="${fieldErrors}">
                        <small class="help-block text-danger text-right"><fmt:message key="${error}" /><br/></small>
                    </c:forEach>
                </div>
            </c:if>

        </c:if>
        <c:if test="${verifyEmail != null}">
            <div class="form-row">
                <label class="col-sm-4 col-form-label" for="inputVerificationCode" ><fmt:message key="app.account.verification_code" />:</label>
                <input id="inputVerificationCode" class="form-control col-sm-8 mb-1" name="verificationCode"
                       placeholder="<fmt:message key="app.account.enter_verification_code" />"
                       required="" autofocus="" type="text">
            </div>
        </c:if>

        <div class="form-row">
            <input hidden name="_FID" type="text" value="${FID}">
        </div>

        <div class="row">
            <c:if test="${verifyEmail != null}">
                <div class="col-4">
                    <a class="btn btn-dark btn-block m-1" role="button"
                       href="${pageContext.request.contextPath}/account/recovery" >
                        <fmt:message key="app.common.cancel" />
                    </a>
                </div>
                <div class="col-4">
                    <a class="btn btn-dark btn-block m-1" role="button"
                       href="${pageContext.request.contextPath}/account/recovery?back">
                        <fmt:message key="app.common.back" />
                    </a>
                </div>
                <div class="col-4">
                    <button class="btn btn-dark btn-block m-1" type="submit" name="btnRecovery"
                            formaction="${pageContext.request.contextPath}/account/recovery/confirm" formmethod="post">
                        <fmt:message key="app.account.recovery" />
                    </button>
                </div>
            </c:if>
            <c:if test="${verifyEmail == null}">
                <div class="col-3"></div>
                <div class="col-6">
                    <button class="btn btn-dark btn-block m-1" type="submit" name="btnNext"
                            formaction="${pageContext.request.contextPath}/account/recovery" formmethod="post">
                        <fmt:message key="app.common.next" />
                    </button>
                </div>
                <div class="col-3"></div>
            </c:if>
        </div>
    </form>
<%@ include file="../common/JSlibs.jsp" %>
<%@ include file="../common/Footer.jsp" %>
