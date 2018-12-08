<%@ include file="../common/Header.jsp" %>
    <h1 class="h3 mb-3 font-weight-normal text-center"><fmt:message key="app.account.profile" /></h1>
    <form class="form-registration text-left col-12" method="post">
        <div class="form-row">
            <%@ include file = "../common/Alert.jsp" %>
        </div>
        <div class="form-row">
            <label class="col-sm-4 col-form-label" for="inputFirstName"><fmt:message key="app.account.first_name" /></label>
            <input id="inputFirstName"
                   class="form-control col-sm-8 mb-1 ${(isInvalid) ? 'is-invalid' : ''}"
                   placeholder="<fmt:message key="app.account.enter_your_first_name" />" required="" autofocus="" type="text"
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
            <label class="col-sm-4 col-form-label" for="inputLastName" ><fmt:message key="app.account.last_name" /></label>
            <input id="inputLastName"
                   class="form-control col-sm-8 mb-1 ${(isInvalid) ? 'is-invalid' : ''}"
                   placeholder="<fmt:message key="app.account.enter_your_last_name" />" required="" autofocus="" type="text"
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
        <c:set value='${errors != null && errors.get("role") != null}' var="isInvalid" />
        <div class="form-row">
            <label class="col-sm-4 col-form-label" for="selectRole" ><fmt:message key="app.account.role" /></label>
            <select id="selectRole" name="role"
                    class="custom-select form-control col-sm-8 mb-1 ${(isInvalid) ? 'is-invalid' : ''}"
                    <c:if test="${verifyEmail != null}">disabled</c:if>>
                <option disabled><fmt:message key="app.account.choose_a_role" /></option>
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
        <c:if test="${userDto.role == 'STUDENT'}">
            <c:set value='${errors != null && errors.get("specialty") != null}' var="isInvalid" />
            <div class="form-row">
                <label class="col-sm-4 col-form-label" for="inputSpecialty" ><fmt:message key="app.account.specialty" /></label>
                <input id="inputSpecialty"
                       class="form-control col-sm-8 mb-1 ${(isInvalid) ? 'is-invalid' : ''}"
                       name="specialty"
                       placeholder="<fmt:message key="app.account.enter_your_specialty" />" required="" autofocus="" type="text"
                       value="<c:out value="${userDto.specialty}"/>"
                       <c:if test="${verifyEmail != null}">disabled</c:if>>
            </div>
            <c:if test="${isInvalid}">
                <c:set value='${errors.get("specialty")}' var="fieldErrors" />
                <div class="form-group text-right">
                    <c:forEach var="error" items="${fieldErrors}">
                        <small class="help-block text-danger text-right"><fmt:message key="${error}" /><br/></small>
                    </c:forEach>
                </div>
            </c:if>

            <c:set value='${errors != null && errors.get("group") != null}' var="isInvalid" />
            <div class="form-row">
                <label class="col-sm-4 col-form-label" for="inputGroup" ><fmt:message key="app.account.group" /></label>
                <input id="inputGroup"
                       class="form-control col-sm-8 mb-1 ${(isInvalid) ? 'is-invalid' : ''}"
                       name="group"
                       placeholder="<fmt:message key="app.account.enter_your_group" />" required="" autofocus="" type="text"
                       value="<c:out value="${userDto.group}"/>"
                       <c:if test="${verifyEmail != null}">disabled</c:if>>
            </div>
            <c:if test="${isInvalid}">
                <c:set value='${errors.get("group")}' var="fieldErrors" />
                <div class="form-group text-right">
                    <c:forEach var="error" items="${fieldErrors}">
                        <small class="help-block text-danger text-right"><fmt:message key="${error}" /><br/></small>
                    </c:forEach>
                </div>
            </c:if>

            <c:set value='${errors != null && errors.get("year") != null}' var="isInvalid" />
            <div class="form-row">
                <label class="col-sm-4 col-form-label" for="inputYear" ><fmt:message key="app.account.year" /></label>
                <input id="inputYear"
                       class="form-control col-sm-8 mb-1 ${(isInvalid) ? 'is-invalid' : ''}"
                       name="year"
                       placeholder="<fmt:message key="app.account.enter_your_year" />" required="" autofocus="" type="text"
                       pattern="\d{1,4}" title="<fmt:message key="app.account.only_digits" />"
                       value="<c:out value="${userDto.year}"/>"
                       <c:if test="${verifyEmail != null}">disabled</c:if>>
            </div>
            <c:if test="${isInvalid}">
                <c:set value='${errors.get("year")}' var="fieldErrors" />
                <div class="form-group text-right">
                    <c:forEach var="error" items="${fieldErrors}">
                        <small class="help-block text-danger text-right"><fmt:message key="${error}" /><br/></small>
                    </c:forEach>
                </div>
            </c:if>
        </c:if>
        <c:if test="${userDto.role == 'TUTOR'}">
            <c:set value='${errors != null && errors.get("position") != null}' var="isInvalid" />
            <div class="form-row">
                <label class="col-sm-4 col-form-label" for="inputPosition" ><fmt:message key="app.account.position" /></label>
                <input id="inputPosition"
                       class="form-control col-sm-8 mb-1 ${(isInvalid) ? 'is-invalid' : ''}"
                       name="position"
                       placeholder="<fmt:message key="app.account.enter_your_position" />" required="" autofocus="" type="text"
                       value="<c:out value="${userDto.position}"/>"
                       <c:if test="${verifyEmail != null}">disabled</c:if>>
            </div>
            <c:if test="${isInvalid}">
                <c:set value='${errors.get("position")}' var="fieldErrors" />
                <div class="form-group text-right">
                    <c:forEach var="error" items="${fieldErrors}">
                        <small class="help-block text-danger text-right"><fmt:message key="${error}" /><br/></small>
                    </c:forEach>
                </div>
            </c:if>

            <c:set value='${errors != null && errors.get("scientificDegree") != null}' var="isInvalid" />
            <div class="form-row">
                <label class="col-sm-4 col-form-label" for="inputScientificDegree" ><fmt:message key="app.account.scientific_degree" /></label>
                <input id="inputScientificDegree"
                       class="form-control col-sm-8 mb-1 ${(isInvalid) ? 'is-invalid' : ''}"
                       name="scientificDegree"
                       placeholder="<fmt:message key="app.account.enter_your_scientific_degree" />" required="" autofocus="" type="text"
                       value="<c:out value="${userDto.scientificDegree}"/>"
                       <c:if test="${verifyEmail != null}">disabled</c:if>>
            </div>
            <c:if test="${isInvalid}">
                <c:set value='${errors.get("scientificDegree")}' var="fieldErrors" />
                <div class="form-group text-right">
                    <c:forEach var="error" items="${fieldErrors}">
                        <small class="help-block text-danger text-right"><fmt:message key="${error}" /><br/></small>
                    </c:forEach>
                </div>
            </c:if>

            <c:set value='${errors != null && errors.get("academicTitle") != null}' var="isInvalid" />
            <div class="form-row">
                <label class="col-sm-4 col-form-label" for="inputAcademicTitle" ><fmt:message key="app.account.academic_title" /></label>
                <input id="inputAcademicTitle"
                       class="form-control col-sm-8 mb-1 ${(isInvalid) ? 'is-invalid' : ''}"
                       name="academicTitle"
                       placeholder="<fmt:message key="app.account.enter_your_academic_title" />" required="" autofocus="" type="text"
                       value="<c:out value="${userDto.academicTitle}"/>"
                       <c:if test="${verifyEmail != null}">disabled</c:if>>
            </div>
            <c:if test="${isInvalid}">
                <c:set value='${errors.get("academicTitle")}' var="fieldErrors" />
                <div class="form-group text-right">
                    <c:forEach var="error" items="${fieldErrors}">
                        <small class="help-block text-danger text-right"><fmt:message key="${error}" /><br/></small>
                    </c:forEach>
                </div>
            </c:if>
        </c:if>

        <c:if test="${verifyEmail == null}">
            <c:set value='${errors != null && errors.get("password") != null}' var="isInvalid" />
            <div class="form-row">
                <label class="col-sm-4 col-form-label" for="inputPassword" ><fmt:message key="app.account.change_password" /></label>
                <input id="inputPassword"
                       class="form-control col-sm-8 mb-1 ${(isInvalid) ? 'is-invalid' : ''}"
                       name="password"
                       value="<c:out value="${userDto.password}"/>"
                       placeholder="<fmt:message key="app.account.enter_your_new_password" />" required="" type="password">
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
                       placeholder="<fmt:message key='app.account.enter_verification_code' />" required="" autofocus="" type="text">
            </div>
        </c:if>

        <div class="row">
        <c:if test="${verifyEmail != null}">
            <div class="col-4">
                <button class="btn btn-dark btn-block m-1" type="submit" name="btnCancel" formnovalidate
                        formaction="${pageContext.request.contextPath}/account/myprofile">
                    <fmt:message key='app.common.cancel' />
                </button>
            </div>
            <div class="col-4">
                <button class="btn btn-dark btn-block m-1" type="submit" name="btnBack" formnovalidate
                        formaction="${pageContext.request.contextPath}/account/myprofile/back">
                    <fmt:message key='app.common.back' />
                </button>
            </div>
            <div class="col-4">
                <button class="btn btn-dark btn-block m-1" type="submit" name="btnConfirm"
                        formaction="${pageContext.request.contextPath}/account/myprofile/confirm">
                    <fmt:message key='app.common.confirm' />
                </button>
            </div>
        </c:if>
        <c:if test="${verifyEmail == null}">
            <div class="col-3"></div>
            <div class="col-6">
                <button class="btn btn-dark btn-block m-1" type="submit" name="btnSave"
                        formaction="${pageContext.request.contextPath}/account/myprofile/verify">
                    <fmt:message key='app.common.save' />
                </button>
            </div>
            <div class="col-3"></div>
        </c:if>
        </div>
    </form>
<%@ include file="../common/JSlibs.jsp" %>
<script src="${pageContext.request.contextPath}/resources/js/MyProfile.js"></script>
<%@ include file="../common/Footer.jsp" %>
