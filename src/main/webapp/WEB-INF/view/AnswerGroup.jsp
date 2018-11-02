<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="input-group mt-1">
    <input id="inputAnswer" class="form-control" name="answers[]"
           placeholder="Enter answer of the question" required autofocus="" type="text"
            value="<c:out value="${answer.description}"/>">
    <div class="input-group-append">
        <div class="input-group-text">
            <input type="checkbox" name="corrects[][]" title="Is the answer correct? Check if Yes."
            <c:if test="${answer.correct}">checked</c:if>>
        </div>
        <button class="btn btn-success" type="button" title="Add answer" data-role="add answer">+</button>
        <button class="btn btn-danger" type="button" title="Remove answer" data-role="remove answer">-</button>
    </div>
</div>