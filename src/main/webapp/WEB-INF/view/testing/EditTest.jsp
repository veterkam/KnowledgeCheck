<%@ include file="../common/Header.jsp" %>
    <h1 class="h3 mb-3 font-weight-normal"><fmt:message key="app.testing.edit_test"/></h1>

    <form class="form-text-edit text-left" data-role="test-container" method="post">
        <div class="form-group">
            <%@ include file = "../common/Alert.jsp" %>
        </div>
        <div class="form-group">
            <input type="text" name="testId" value="${test.testId}" hidden>
        </div>

        <c:set value='${test.errors != null && test.errors.get("title") != null}' var="isInvalid" />
        <div class="form-group">
            <label for="inputTitle"><fmt:message key="app.testing.title"/>:</label>
            <input id="inputTitle" name="title"
                   class="form-control ${(isInvalid) ? 'is-invalid' : ''}"
                   placeholder='<fmt:message key="app.testing.enter_title_of_test"/>' required="" autofocus="" type="text"
                   value="<c:out value="${test.title}"/>">
        </div>
        <c:if test="${isInvalid}">
            <c:set value='${test.errors.get("title")}' var="fieldErrors" />
            <div class="form-group text-right" data-role="field-error">
                <c:forEach var="error" items="${fieldErrors}">
                    <small class="help-block text-danger text-right"><fmt:message key="${error}" /><br/></small>
                </c:forEach>
            </div>
        </c:if>

        <c:set value='${test.errors != null && test.errors.get("subjectId") != null}' var="isInvalid" />
        <div class="form-group">
            <label for="selectSubject" ><fmt:message key="app.testing.subject"/>:</label>
            <select id="selectSubject" name="subjectId" placeholder='<fmt:message key="app.testing.choose_a_subject"/>'
                    class="custom-select form-control ${(isInvalid) ? 'is-invalid' : ''}" required="" >
                <option disabled
                        <c:if test="${test==null || test.subjectId==null}">selected</c:if>>
                    <fmt:message key="app.testing.choose_a_subject"/>
                </option>
                <c:forEach var="subject" items="${subjects}">
                    <option <c:if test="${subject.id == test.subjectId}">selected</c:if> value="${subject.id}">
                        <c:out value="${subject.name}"/>
                    </option>
                </c:forEach>
            </select>
        </div>
        <c:if test="${isInvalid}">
            <c:set value='${test.errors.get("subjectId")}' var="fieldErrors" />
            <div class="form-group text-right" data-role="field-error">
                <c:forEach var="error" items="${fieldErrors}">
                    <small class="help-block text-danger text-right"><fmt:message key="${error}" /><br/></small>
                </c:forEach>
            </div>
        </c:if>

        <c:set value='${test.errors != null && test.errors.get("duration") != null}' var="isInvalid" />
        <div class="form-group">
            <label for="inputDuration"><fmt:message key="app.testing.time_limitation"/>:</label>
            <input id="inputDuration"
                   name="duration"
                   class="form-control ${(isInvalid) ? 'is-invalid' : ''}"
                   placeholder='__:__:__' required="" autofocus="" type="text"
                   value="<c:out value="${test.duration}"/>">
        </div>
        <c:if test="${isInvalid}">
            <c:set value='${test.errors.get("duration")}' var="fieldErrors" />
            <div class="form-group text-right" data-role="field-error">
                <c:forEach var="error" items="${fieldErrors}">
                    <small class="help-block text-danger text-right"><fmt:message key="${error}" /><br/></small>
                </c:forEach>
            </div>
        </c:if>

        <c:set value='${test.errors != null && test.errors.get("description") != null}' var="isInvalid" />
        <div class="form-group">
            <label for="inputDescription"><fmt:message key="app.testing.description"/>:</label>
            <textarea class="form-control ${(isInvalid) ? 'is-invalid' : ''}"
                      rows="3" id="inputDescription" name="description"
                      placeholder='<fmt:message key="app.testing.enter_description_of_test"/>'
                      autofocus=""><c:out value="${test.description}"/></textarea>

        </div>
        <c:if test="${isInvalid}">
            <c:set value='${test.errors.get("description")}' var="fieldErrors" />
            <div class="form-group text-right" data-role="field-error">
                <c:forEach var="error" items="${fieldErrors}">
                    <small class="help-block text-danger text-right"><fmt:message key="${error}" /><br/></small>
                </c:forEach>
            </div>
        </c:if>

        <c:choose>
            <c:when test="${fn:length(test.questions) > 0}">
                <c:forEach var="question" items="${test.questions}">
                    <%@ include file = "QuestionContainer.jsp" %>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <%@ include file = "QuestionContainer.jsp" %>
            </c:otherwise>
        </c:choose>


        <div class="form-group">
            <input hidden name="_FID" type="text" value="${FID}">
        </div>

        <div class="form-group text-right">
            <a href="${pageContext.request.contextPath}/testing/mytests"
               class="btn btn-dark m-1" role="button">
                <fmt:message key="app.common.cancel"/>
            </a>
            <button class="btn btn-dark m-1" type="submit" name="btnSave" data-role="indexing"
                    formaction="${pageContext.request.contextPath}/testing/edit">
                <fmt:message key="app.testing.save_test"/>
            </button>
        </div>

    </form>
<%@ include file="../common/JSlibs.jsp" %>
<script src="${pageContext.request.contextPath}/resources/js/EditTest.js"></script>
<%@ include file="../common/Footer.jsp" %>
