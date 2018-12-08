<%@ include file="../common/Header.jsp" %>
    <h1 class="h3 mb-3 font-weight-normal">Profile</h1>
    <c:if test="${anonym != null}"><c:set value="${anonym}" var="userData"></c:set> </c:if>
    <c:if test="${anonym == null}"><c:set value="${user}" var="userData"></c:set> </c:if>
    <form class="form-registration text-left col-12" method="post">
        <div class="form-row">
            <%@ include file = "../common/Alert.jsp" %>
        </div>
        <div class="form-row">
            <label class="col-sm-4 col-form-label" for="inputFirstName">First Name:</label>
            <input id="inputFirstName" class="form-control col-sm-8 mb-1" name="firstName"
                   placeholder="Enter your first name" required="" autofocus="" type="text"
                   value="<c:out value="${userData.firstName}"/>"
                   <c:if test="${verifyEmail != null}">disabled</c:if>>
        </div>
        <div class="form-row">
            <label class="col-sm-4 col-form-label" for="inputLastName" >Last Name:</label>
            <input id="inputLastName" class="form-control col-sm-8 mb-1" name="lastName"
                   placeholder="Enter your last name" required="" autofocus="" type="text"
                   value="<c:out value="${userData.lastName}"/>"
                   <c:if test="${verifyEmail != null}">disabled</c:if>>
        </div>
        <div class="form-row">
            <label class="col-sm-4 col-form-label" for="inputUsername" >Username:</label>
            <input id="inputUsername" class="form-control col-sm-8 mb-1" name="username"
                   placeholder="Enter your username" required="" autofocus="" type="username"
                   value="<c:out value="${userData.username}"/>"
                   disabled>
        </div>
        <div class="form-row">
            <label class="col-sm-4 col-form-label" for="inputEmail" >E-mail:</label>
            <input id="inputEmail" class="form-control col-sm-8 mb-1" name="email"
                   placeholder="Enter your real e-mail address" required="" autofocus="" type="email"
                   value="<c:out value="${userData.email}"/>"
                   <c:if test="${verifyEmail != null}">disabled</c:if>>
        </div>
        <div class="form-row">
            <label class="col-sm-4 col-form-label" for="selectRole" >Role:</label>
            <select id="selectRole" name="role" class="custom-select form-control col-sm-8 mb-1" disabled>
                <option disabled>Choose a role</option>
                <c:forEach var="role" items="${roles}">
                    <option <c:if test="${role == userData.role}">selected</c:if>  value="${role}">
                        <c:out value="${role}"/>
                    </option>
                </c:forEach>
            </select>
        </div>
        <c:if test="${userData.role == 'STUDENT'}">
            <div class="form-row">
                <label class="col-sm-4 col-form-label" for="inputSpecialty" >Specialty:</label>
                <input id="inputSpecialty" class="form-control col-sm-8 mb-1" name="specialty"
                       placeholder="Enter your specialty" required="" autofocus="" type="text"
                       value="<c:out value="${userData.specialty}"/>"
                       <c:if test="${verifyEmail != null}">disabled</c:if>>
            </div>
            <div class="form-row">
                <label class="col-sm-4 col-form-label" for="inputGroup" >Group:</label>
                <input id="inputGroup" class="form-control col-sm-8 mb-1" name="group"
                       placeholder="Enter your group" required="" autofocus="" type="text"
                       value="<c:out value="${userData.group}"/>"
                       <c:if test="${verifyEmail != null}">disabled</c:if>>
            </div>
            <div class="form-row">
                <label class="col-sm-4 col-form-label" for="inputYear" >Year:</label>
                <input id="inputYear" class="form-control col-sm-8 mb-1" name="year"
                       placeholder="Enter your year" required="" autofocus="" type="text"
                       pattern="\d{1,4}" title="Only 1-4 digits"
                       value="<c:out value="${userData.year}"/>"
                       <c:if test="${verifyEmail != null}">disabled</c:if>>
            </div>
        </c:if>
        <c:if test="${userData.role == 'TUTOR'}">
            <div class="form-row">
                <label class="col-sm-4 col-form-label" for="inputPosition" >Position:</label>
                <input id="inputPosition" class="form-control col-sm-8 mb-1" name="position"
                       placeholder="Enter your position" required="" autofocus="" type="text"
                       value="<c:out value="${userData.position}"/>"
                       <c:if test="${verifyEmail != null}">disabled</c:if>>
            </div>
            <div class="form-row">
                <label class="col-sm-4 col-form-label" for="inputScientificDegree" >Scientific degree:</label>
                <input id="inputScientificDegree" class="form-control col-sm-8 mb-1" name="scientificDegree"
                       placeholder="Enter your scientific degree" required="" autofocus="" type="text"
                       value="<c:out value="${userData.scientificDegree}"/>"
                       <c:if test="${verifyEmail != null}">disabled</c:if>>
            </div>
            <div class="form-row">
                <label class="col-sm-4 col-form-label" for="inputAcademicTitle" >Academic title:</label>
                <input id="inputAcademicTitle" class="form-control col-sm-8 mb-1" name="academicTitle"
                       placeholder="Enter your academic title" required="" autofocus="" type="text"
                       value="<c:out value="${userData.academicTitle}"/>"
                       <c:if test="${verifyEmail != null}">disabled</c:if>>
            </div>
        </c:if>
        <c:if test="${verifyEmail == null}">
            <div class="form-row">
                <label class="col-sm-4 col-form-label" for="inputPassword" >New password:</label>
                <input id="inputPassword" class="form-control col-sm-8 mb-1" name="password"
                       placeholder="Enter new password for change it" type="password">
            </div>
            <div class="form-row">
                <label class="col-sm-4 col-form-label" for="inputRepeatPassword" class="sr-only">Repeat Password:</label>
                <input id="inputRepeatPassword" class="form-control col-sm-8 mb-1" name="repeatPassword"
                       placeholder="Repeat your password" required="" type="password" disabled>
            </div>
        </c:if>
        <c:if test="${verifyEmail != null}">
            <div class="form-row">
                <label class="col-sm-4 col-form-label" for="inputVerificationCode" >Verification code:</label>
                <input id="inputVerificationCode" class="form-control col-sm-8 mb-1" name="verificationCode"
                       placeholder="Enter verification code" required="" autofocus="" type="text">
            </div>
        </c:if>

        <div class="row">
        <c:if test="${verifyEmail != null}">
            <div class="col-4">
                <button class="btn btn-dark btn-block m-1" type="submit" name="btnCancel" formnovalidate
                        formaction="${pageContext.request.contextPath}/account/myprofile">
                    Cancel
                </button>
            </div>
            <div class="col-4">
                <button class="btn btn-dark btn-block m-1" type="submit" name="btnBack" formnovalidate
                        formaction="${pageContext.request.contextPath}/account/myprofile/back">
                    Back
                </button>
            </div>
            <div class="col-4">
                <button class="btn btn-dark btn-block m-1" type="submit" name="btnConfirm"
                        formaction="${pageContext.request.contextPath}/account/myprofile/confirm">
                    Confirm
                </button>
            </div>
        </c:if>
        <c:if test="${verifyEmail == null}">
            <div class="col-3"></div>
            <div class="col-6">
                <button class="btn btn-dark btn-block m-1" type="submit" name="btnSave"
                        formaction="${pageContext.request.contextPath}/account/myprofile/verify">
                    Save
                </button>
            </div>
            <div class="col-3"></div>
        </c:if>
        </div>
    </form>
<%@ include file="../common/JSlibs.jsp" %>
<script src="${pageContext.request.contextPath}/resources/js/MyProfile.js"></script>
<%@ include file="../common/Footer.jsp" %>
