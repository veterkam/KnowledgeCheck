<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="Header.jsp" %>
<%@ include file = "NavBar.jsp" %>
<div class="container">
    <h1 class="h3 mb-3 font-weight-normal">Edit test</h1>
    <%@ include file = "Alert.jsp" %>
    <form class="form-text-edit text-left" method="get">
        <div class="form-group">
            <label for="inputTitle">Title:</label>
            <input id="inputTitle" class="form-control" name="title"
                   placeholder="Enter title of test" required="" autofocus="" type="text"
                   value="<c:out value="${test.title}"/>">
        </div>
        <div class="form-group">
            <label for="selectSubject" >Subject:</label>
            <select id="selectSubject" name="subject" placeholder="Choose a subject" class="custom-select form-control" required="" >
                <option disabled>Choose a subject</option>
                <c:forEach var="subject" items="${subjects}">
                    <option <c:if test="${subject.id == test.subjectId}">selected</c:if>  value="${subject.id}">
                        <c:out value="${subject.name}"/>
                    </option>
                </c:forEach>
            </select>
        </div>
        <div class="form-group">
            <label for="inputDescription">Description:</label>
            <textarea class="form-control" rows="5" id="inputDescription" name="description"
                placeholder="Enter description of test" autofocus=""><c:out value="${test.description}"/></textarea>
        </div>

        <c:choose>
            <c:when test="${fn:length(test.questions) > 0}">
                <c:forEach var="question" items="${test.questions}">
                    <%@ include file = "QuestionCard.jsp" %>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <%@ include file = "QuestionCard.jsp" %>
            </c:otherwise>
        </c:choose>


        <div class="form-group text-right">
            <button class="btn btn-dark m-1" type="submit" name="btnSave" data-role="indexing"
                    formaction="${pageContext.request.contextPath}/testboard/add">
                Save Test
            </button>
        </div>

    </form>
</div>
<%@ include file="Footer.jsp" %>
