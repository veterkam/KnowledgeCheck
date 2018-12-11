<%@ include file="../common/Header.jsp" %>
    <h1 class="h3 mb-3 font-weight-normal"><fmt:message key="app.testing.edit_test"/></h1>

    <form class="form-text-edit text-left" data-role="test-container" method="post">
        <div class="form-group">
            <%@ include file = "../common/Alert.jsp" %>
        </div>
        <div class="form-group">
            <input type="text" name="testId" value="${test.testId}" hidden>
        </div>
        <div class="form-group">
            <label for="inputTitle"><fmt:message key="app.testing.title"/>:</label>
            <input id="inputTitle" class="form-control" name="title"
                   placeholder='<fmt:message key="app.testing.enter_title_of_test"/>' required="" autofocus="" type="text"
                   value="<c:out value="${test.title}"/>">
        </div>
        <div class="form-group">
            <label for="selectSubject" ><fmt:message key="app.testing.subject"/>:</label>
            <select id="selectSubject" name="subjectId" placeholder='<fmt:message key="app.testing.choose_a_subject"/>' class="custom-select form-control" required="" >
                <option disabled <c:if test="${test==null}">selected</c:if>><fmt:message key="app.testing.choose_a_subject"/></option>
                <c:forEach var="subject" items="${subjects}">
                    <option <c:if test="${subject.id == test.subjectId}">selected</c:if> value="${subject.id}">
                        <c:out value="${subject.name}"/>
                    </option>
                </c:forEach>
            </select>

        </div>
        <div class="form-group">
            <label for="inputDescription"><fmt:message key="app.testing.description"/>:</label>
            <textarea class="form-control" rows="5" id="inputDescription" name="description"
                placeholder='<fmt:message key="app.testing.enter_description_of_test"/>' autofocus=""><c:out value="${test.description}"/></textarea>

        </div>

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
