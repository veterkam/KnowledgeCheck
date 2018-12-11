<form class="form-inline mb-2  justify-content-center" action="">
    <label for="subjectFilter"><fmt:message key="app.presentation.filter" />:&nbsp;&nbsp;</label>
    <select id="subjectFilter" name="presentationSubjectId" class="custom-select form-control mr-4">
        <option value="0"><fmt:message key="app.presentation.all_subjects" /></option>
        <c:forEach var="subject" items="${presentationSubjects}">
            <option <c:if test="${subject.id == presentationSubjectId}">selected</c:if> value="${subject.id}">
                <c:out value="${subject.name}"/>
            </option>
        </c:forEach>
    </select>
    <label for="dateOrder"><fmt:message key="app.presentation.order" />:&nbsp;&nbsp;</label>
    <select id="dateOrder" name="presentationDateOrder" class="custom-select form-control mr-4">
        <c:forEach var="order" items="${presentationOrders}">
            <option <c:if test="${order eq presentationDateOrder}">selected</c:if>  value="${order}">
                <fmt:message key="${order}" />
            </option>
        </c:forEach>
    </select>
    <button type="submit" class="btn btn-dark btn-sm">
        <fmt:message key="app.presentation.apply" />
    </button>
</form>
