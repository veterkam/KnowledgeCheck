<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file = "../common/Header.jsp" %>
<%@ include file = "../common/NavBar.jsp" %>
<div class="container text-left">

    <div class="row">
        <div class="col">
            <h1 class="h3 mb-3 font-weight-normal">Students results</h1>
        </div>
    </div>

    <%@ include file="../common/Alert.jsp" %>

    <c:if test="${fn:length(tests) == 0}">
        <div class="row">
            <div class="col">
                <h3 class="h5 mb-3 font-weight-normal">
                    You don't have any test.&nbsp;<a href="${pageContext.request.contextPath}/testboard/edit">New test</a>
                </h3>
            </div>
        </div>
    </c:if>
    <c:if test="${fn:length(tests) > 0}">

        <div class="row">
            <div class="col">
                <%@ include file="Presentation.jsp" %>
            </div>
        </div>
        <div class="row">
            <div class="col">
                <%@ include file="../common/Pagination.jsp" %>
            </div>
        </div>
        <c:forEach var="test" items="${tests}" varStatus="testLoop">
            <div class="row">
                <div class="col">
                    <div class="card mb-4">
                        <div class="card-header">
                            <div class="row">
                                <div class="col text-left">
                                    <c:if test="${test.subject != null}">
                                        <strong>Subject</strong>: <c:out value="${test.subject.name}"/>
                                    </c:if>
                                </div>
                                <div class="col text-right">
                                    <c:if test="${test.tutor != null}">
                                        <strong>Author</strong>: <c:out value="${test.tutor.firstname} ${test.tutor.lastname}"/>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                        <div class="card-body">
                            <h5 class="card-title"><c:out value="${test.title}"/></h5>
                            <p class="card-text"><c:out value="${test.description}"/></p>
                            <div id="desc${testLoop.index}" class="collapse">
                                <c:forEach var="studentTestingResults"  items="${testingResultsList[testLoop.index]}" varStatus="studentTestingResultsLoop">
                                    <div class="card mt-1">
                                        <div class="card-header">
                                            <div class="row">
                                                <div class="col text-left">
                                                    <p class="card-text">
                                                        <strong>${studentTestingResultsLoop.count}.
                                                            <a href="${pageContext.request.contextPath}/authorization/profile?id=${studentTestingResults.student.id}">
                                                                <c:out value="${studentTestingResults.student.fullname}"/></a>
                                                        </strong>
                                                        ${studentTestingResults.score}% correct answers
                                                    </p>
                                                </div>
                                                <div class="col text-right">
                                                    <button type="button" class="btn btn-dark btn-sm m-1" data-toggle="collapse"
                                                            data-target="#testingResults${testLoop.index}_${studentTestingResultsLoop.index}">
                                                        Results
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="card-body collapse" id="testingResults${testLoop.index}_${studentTestingResultsLoop.index}">
                                            <c:forEach var="question" items="${test.questions}" varStatus="questionLoop">
                                                <div class="row">
                                                    <div class="col-sm-1">
                                                        ${questionLoop.count}.
                                                    </div>
                                                    <div class="col-sm-10">
                                                        <c:out value="${question.description}"/>
                                                    </div>
                                                    <div class="col-sm-1">
                                                        <c:if test="${studentTestingResults.answerResults[question.id] == true}">
                                                            &#x2714;
                                                        </c:if>
                                                        <c:if test="${studentTestingResults.answerResults[question.id] == false}">
                                                            &#x2718;
                                                        </c:if>
                                                    </div>
                                                </div>
                                             </c:forEach>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                        <div class="card-footer text-muted">
                            <div class="row">
                                <div class="col text-left">
                                    <fmt:formatDate type = "both" timeStyle = "short" value = "${test.updateTime}" />
                                </div>
                                <div class="col text-right">
                                    <button type="button" class="btn btn-dark btn-sm m-1" data-toggle="collapse"
                                            data-target="#desc${testLoop.index}"
                                            <c:if test="${fn:length(testingResultsList[testLoop.index]) == 0}">disabled</c:if>>
                                        Details
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
        <div class="row">
            <div class="col">
                <%@ include file="../common/Pagination.jsp" %>
            </div>
        </div>
    </c:if>
</div>
<%@ include file="../common/JSlibs.jsp" %>
<%@ include file="../common/Footer.jsp" %>
