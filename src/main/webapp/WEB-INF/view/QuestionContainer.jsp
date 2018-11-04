<div class="card mb-2" data-role="question-container">
    <div class="card-body">
        <div class="form-group">
            <input hidden type="text" data-role="question-id" name="questionIds" value="${question.id}">
            <input hidden type="text" data-role="question-removing-flag" name="questionRemovingFlags" value="0">
            <label for="inputQuestion">Question:</label>
            <textarea class="form-control" rows="5" id="inputQuestion" name="questions" required
                      placeholder="Enter question" autofocus=""><c:out value="${question.description}"/></textarea>

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
            <button class="btn btn-dark mr-2 mt-4" type="button" data-role="add-question">Add question</button>
            <button class="btn btn-dark mt-4" type="button" data-role="remove-question">Remove question</button>
        </div>
    </div>
</div>
