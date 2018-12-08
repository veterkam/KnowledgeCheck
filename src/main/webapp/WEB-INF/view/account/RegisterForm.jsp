<%@ include file="../common/Header.jsp" %>
    <h1 class="h3 mb-3 font-weight-normal text-center">Register</h1>

    <form class="form-register text-left col-12">
        <%@ include file = "../common/Alert.jsp" %>

        <c:set value='${errors != null && errors.get("firstName") != null}' var="isInvalid" />
        <div class="form-row">
            <label class="col-sm-4 col-form-label" for="inputFirstName">First Name:</label>
            <input id="inputFirstName"
                   class="form-control col-sm-8 mb-1 ${(isInvalid) ? 'is-invalid' : ''}"
                   placeholder="Enter your first name" required="" autofocus="" type="text"
                   name="firstName" value="<c:out value="${userDto.firstName}"/>"
                   <c:if test="${verifyEmail != null}">disabled</c:if>>
        </div>
        <c:if test="${isInvalid}">
            <c:set value='${errors.get("firstName")}' var="fieldErrors" />
            <div class="form-group text-right">
                <c:forEach var="error" items="${fieldErrors}">
                    <small class="help-block text-danger text-right"><fmt:message key="${error}" /><br/></small>
                </c:forEach>
            </div>
        </c:if>

        <c:set value='${errors != null && errors.get("lastName") != null}' var="isInvalid" />
        <div class="form-row">
            <label class="col-sm-4 col-form-label" for="inputLastName" >Last Name:</label>
            <input id="inputLastName"
                   class="form-control col-sm-8 mb-1 ${(isInvalid) ? 'is-invalid' : ''}"
                   placeholder="Enter your last name" required="" autofocus="" type="text"
                   name="lastName" value="<c:out value="${userDto.lastName}"/>"
                   <c:if test="${verifyEmail != null}">disabled</c:if>>
        </div>
        <c:if test="${isInvalid}">
            <c:set value='${errors.get("lastName")}' var="fieldErrors" />
            <div class="form-group text-right">
                <c:forEach var="error" items="${fieldErrors}">
                    <small class="help-block text-danger text-right"><fmt:message key="${error}" /><br/></small>
                </c:forEach>
            </div>
        </c:if>

        <c:set value='${errors != null && errors.get("username") != null}' var="isInvalid" />
        <div class="form-row">
            <label class="col-sm-4 col-form-label" for="inputUsername" >Username:</label>
            <input id="inputUsername"
                   class="form-control col-sm-8 mb-1 ${(isInvalid) ? 'is-invalid' : ''}"
                   placeholder="Enter your username" required="" autofocus="" type="username"
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
            <label class="col-sm-4 col-form-label" for="inputEmail" >E-mail:</label>
            <input id="inputEmail"
                   class="form-control col-sm-8 mb-1 ${(isInvalid) ? 'is-invalid' : ''}"
                   name="email"
                   placeholder="Enter your real e-mail address" required="" autofocus="" type="email"
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
                <label class="col-sm-4 col-form-label" for="inputPassword" >Password:</label>
                <input id="inputPassword"
                       class="form-control col-sm-8 mb-1 ${(isInvalid) ? 'is-invalid' : ''}"
                       name="password"
                       value="<c:out value="${userDto.password}"/>"
                       placeholder="Enter your password" required="" type="password">
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
                <label class="col-sm-4 col-form-label" for="inputConfirmPassword" class="sr-only">Confirm Password:</label>
                <input id="inputConfirmPassword"
                       class="form-control col-sm-8 mb-1 ${(isInvalid) ? 'is-invalid' : ''}"
                       name="confirmPassword"
                       value="<c:out value="${userDto.confirmPassword}"/>"
                       placeholder="Confirm your password" required="" type="password">
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

        <c:set value='${errors != null && errors.get("role") != null}' var="isInvalid" />
        <div class="form-row">
            <label class="col-sm-4 col-form-label" for="selectRole" >Role:</label>
            <select id="selectRole" name="role"
                    class="custom-select form-control col-sm-8 mb-1 ${(isInvalid) ? 'is-invalid' : ''}"
                    <c:if test="${verifyEmail != null}">disabled</c:if>>
                <option disabled>Choose a role</option>
                <c:forEach var="role" items="${roles}">
                    <option <c:if test="${role == userDto.role}">selected</c:if>  value="${role}">
                        <fmt:message key="${role.toString()}" />
                    </option>
                </c:forEach>
            </select>
        </div>
        <c:if test="${isInvalid}">
            <c:set value='${errors.get("role")}' var="fieldErrors" />
            <div class="form-group text-right">
                <c:forEach var="error" items="${fieldErrors}">
                    <small class="help-block text-danger text-right"><fmt:message key="${error}" /><br/></small>
                </c:forEach>
            </div>
        </c:if>
        
        <c:if test="${verifyEmail != null}">
            <div class="form-row">
                <label class="col-sm-4 col-form-label" for="inputVerificationCode" >Verification code:</label>
                <input id="inputVerificationCode" class="form-control col-sm-8 mb-1" name="verificationCode"
                       placeholder="Enter verification code" required="" autofocus="" type="text">
            </div>
        </c:if>

        <div class="row m-3">
        <c:if test="${verifyEmail != null}">
            <div class="col-4">
                <a class="btn btn-dark btn-block m-1" role="button"
                   href="${pageContext.request.contextPath}/account/register" >
                    Cancel
                </a>
            </div>
            <div class="col-4">
                <a class="btn btn-dark btn-block m-1" role="button"
                   href="${pageContext.request.contextPath}/account/register?back">
                    Back
                </a>
            </div>
            <div class="col-4">
                <button class="btn btn-dark btn-block m-1" type="submit" name="btnRegister"
                        formaction="${pageContext.request.contextPath}/account/register/confirm" formmethod="post">
                    Register
                </button>
            </div>
        </c:if>
        <c:if test="${verifyEmail == null}">
            <div class="col-3"></div>
            <div class="col-6">
                <button class="btn btn-dark btn-block m-1" type="submit" name="btnNext"
                        formaction="${pageContext.request.contextPath}/account/register" formmethod="post">
                    Next
                </button>
            </div>
            <div class="col-3"></div>
        </c:if>
        </div>
        <div class="row">
            <div class="col text-center">
                <small class="mt-5">
                    Do you have an account?
                    <a href="${pageContext.request.contextPath}/account/login">Login</a>
                </small>
            </div>
        </div>
    </form>
<%@ include file="../common/JSlibs.jsp" %>
<%@ include file="../common/Footer.jsp" %>
