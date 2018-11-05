<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file = "Header.jsp" %>
<%@ include file = "NavBar.jsp" %>
<div class="container text-left">

    <div class="row">
        <div class="col">
            <h1 class="h3 mb-3 font-weight-normal">My tests</h1>
        </div>
        <div class="col text-right">
            <a href="${pageContext.request.contextPath}/testboard/edit"
               class="btn btn-info btn-dark" role="button">New test</a>
        </div>
    </div>

    <%@ include file="Alert.jsp" %>

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
                <%@ include file="Pagination.jsp" %>
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
                                <c:forEach var="question" items="${test.questions}" varStatus="questionLoop">
                                    <div class="card mb-1">
                                        <div class="card-header">
                                                ${questionLoop.count}. <c:out value="${question.description}"/>
                                        </div>
                                        <div class="card-body">
                                            <c:forEach var="answer" items="${question.answers}" varStatus="answerLoop">
                                                <%--&#x2714;&#x2718;--%>
                                                <p class="card-text">
                                                    ${answerLoop.count}.&nbsp;
                                                    <c:out value="${answer.description}"/>
                                                    <c:if test="${answer.correct}">&nbsp;&#x2714;</c:if>
                                                </p>
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
                                    <a href="${pageContext.request.contextPath}/testboard/edit?testId=${test.id}"
                                       class="btn btn-sm btn-dark m-1" role="button">
                                        Edit test
                                    </a>
                                    <a href="${pageContext.request.contextPath}/testboard/remove?testId=${test.id}"
                                       class="btn btn-sm btn-dark m-1" role="button"
                                       onclick="return confirm('Are you sure you want to remove the test?')">
                                        Remove test
                                    </a>
                                    <button type="button" class="btn btn-dark btn-sm m-1" data-toggle="collapse"
                                            data-target="#desc${testLoop.index}"
                                            <c:if test="${fn:length(test.questions) == 0}">disabled</c:if>>
                                        Description
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
                <%@ include file="Pagination.jsp" %>
            </div>
        </div>
    </c:if>
</div>
<%@ include file="Footer.jsp" %>
