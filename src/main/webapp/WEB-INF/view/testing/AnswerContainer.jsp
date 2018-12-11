<c:set value='${answer.errors != null && answer.errors.get("description") != null}' var="isInvalid" />
<div data-role="answer-container" <c:if test="${answer.isRemoved()}">hidden</c:if> >
    <div class="input-group mt-1" >
        <input hidden type="text" data-role="answer-id" name="answerIds[]" value="${answer.answerId}">
        <input hidden type="text" data-role="answer-removing-flag" name="answerRemovingFlags[]" value="${answer.getRemoved()}">

        <input id="inputAnswer"
               class="form-control ${(isInvalid) ? 'is-invalid' : ''}"
               name="answerDescriptions[]"
               placeholder="<fmt:message key="app.testing.enter_answer_of_the_question"/>" required autofocus="" type="text"
                value="<c:out value="${answer.description}"/>">
        <div class="input-group-append">
            <div class="input-group-text">
                <input type="checkbox" name="corrects[][]" title="<fmt:message key="app.testing.is_the_answer_correct_check_if_yes"/>"
                <c:if test="${answer.isCorrect()}">checked</c:if>>
            </div>
            <button class="btn btn-success" type="button" title="<fmt:message key="app.testing.add_answer"/>" data-role="add-answer">+</button>
            <button class="btn btn-danger" type="button" title="<fmt:message key="app.testing.remove_answer"/>" data-role="remove-answer">-</button>
        </div>
    </div>
    <c:if test="${isInvalid}">
        <c:set value='${answer.errors.get("description")}' var="fieldErrors" />
        <div class="text-right" data-role="field-error">
            <c:forEach var="error" items="${fieldErrors}">
                <small class="help-block text-danger text-right"><fmt:message key="${error}" /><br/></small>
            </c:forEach>
        </div>
    </c:if>
</div>
