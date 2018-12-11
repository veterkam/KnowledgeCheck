<div class="input-group mt-1" data-role="answer-container">
    <input hidden type="text" data-role="answer-id" name="answerIds[]" value="${answer.answerId}">
    <input hidden type="text" data-role="answer-removing-flag" name="answerRemovingFlags[]" value="${answer.getRemoved()}">

    <input id="inputAnswer" class="form-control" name="answerDescriptions[]"
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