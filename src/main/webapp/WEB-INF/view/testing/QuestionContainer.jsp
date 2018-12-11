<div class="card mb-2" data-role="question-container" <c:if test="${question.isRemoved()}">hidden</c:if> >
    <div class="card-body">
        <div class="form-group">
            <input hidden type="text" data-role="question-id" name="questionIds" value="${question.questionId}">
            <input hidden type="text" data-role="question-removing-flag" name="questionRemovingFlags" value="${question.getRemoved()}">
            <label for="inputQuestion"><fmt:message key="app.testing.question"/>:</label>
            <c:set value='${question.errors != null && question.errors.get("description") != null}' var="isInvalid" />
            <textarea class="form-control ${(isInvalid) ? 'is-invalid' : ''}"
                      rows="2" id="inputQuestion" name="questionDescriptions" required
                      placeholder="<fmt:message key="app.testing.enter_question"/>" autofocus=""><c:out value="${question.description}"/></textarea>

            <c:if test="${isInvalid}">
                <c:set value='${question.errors.get("description")}' var="fieldErrors" />
                <div class="text-right" data-role="field-error">
                    <c:forEach var="error" items="${fieldErrors}">
                        <small class="help-block text-danger text-right"><fmt:message key="${error}" /><br/></small>
                    </c:forEach>
                </div>
            </c:if>


            <c:choose>
                <c:when test="${fn:length(question.answers) > 0}">
                    <c:forEach var="answer" items="${question.answers}">
                        <%@ include file = "AnswerContainer.jsp" %>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <%@ include file = "AnswerContainer.jsp" %>
                </c:otherwise>
            </c:choose>

        </div>
        <div class="form-group text-right">
            <button class="btn btn-dark mr-2 mt-4" type="button" data-role="add-question"><fmt:message key="app.testing.add_question"/></button>
            <button class="btn btn-dark mt-4" type="button" data-role="remove-question"><fmt:message key="app.testing.remove_question"/></button>
        </div>
    </div>
</div>
