<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<form class="form-inline mb-2  justify-content-center" action="">
    <label for="subjectFilter">Filter:</label>
    <select id="subjectFilter" name="subjectFilter" class="custom-select form-control mr-4">
        <option value="0">All subjects</option>
        <c:forEach var="subject" items="${presentationSubjects}">
            <option <c:if test="${subject.id == presentationSubjectId}">selected</c:if>  value="${subject.id}">
                <c:out value="${subject.name}"/>
            </option>
        </c:forEach>
    </select>
    <label for="dateOrder">Order:</label>
    <select id="dateOrder" name="dateOrder" class="custom-select form-control mr-4">
        <c:forEach var="order" items="${presentationOrders}">
            <option <c:if test="${order eq presentationDateOrder}">selected</c:if>  value="${order}">
                <c:out value="${order}"/>
            </option>
        </c:forEach>
    </select>
    <button type="submit" class="btn btn-dark btn-sm">
        Apply
    </button>
</form>
