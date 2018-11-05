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
            <h1 class="h3 mb-3 font-weight-normal">Run test</h1>
        </div>
    </div>

    <%@ include file="Alert.jsp" %>
    <div class="row">
        <div class="col">
            <form class="form-text-edit text-left" data-role="test-container" id="test-carousel-container" method="get">
                <input type="text" name="testId" value="${test.id}" hidden>
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
                                    <strong>Author</strong>: <c:out value="${test.tutor.fullname}"/>
                                </c:if>
                            </div>
                        </div>
                    </div>
                    <div class="card-body">
                        <h5 class="card-title"><c:out value="${test.title}"/></h5>
                        <p class="card-text"><c:out value="${test.description}"/></p>
                        <div class="progress mb-4">
                            <div id="test-progress-bar" class="progress-bar bg-dark progress-bar-striped text-center">
                            </div>
                        </div>
                        <div id="question-carousel" class="carousel slide" data-interval="false">
                            <!-- The slideshow -->
                            <div class="carousel-inner">
                                <c:forEach var="question" items="${test.questions}" varStatus="questionLoop">
                                    <div class="carousel-item <c:if test="${questionLoop.count == 1}">active</c:if>">
                                        <div class="card" data-role="question-container">
                                            <div class="card-header">
                                                <input hidden type="text" data-role="question-id" name="questionIds"
                                                       value="${question.id}">
                                                    ${questionLoop.count}. <c:out value="${question.description}"/>
                                            </div>
                                            <div class="card-body">
                                                <c:forEach var="answer" items="${question.answers}" varStatus="answerLoop">
                                                    <div data-role="answer-container">
                                                        <input hidden type="text" data-role="answer-id" name="answerIds[]"
                                                               value="${answer.id}">

                                                        <div class="form-check">
                                                            <label class="form-check-label">
                                                                <input type="checkbox" class="form-check-input" name="checkedAnswers[][]"
                                                                       value=""><c:out value="${answer.description}"/>
                                                            </label>
                                                        </div>
                                                    </div>
                                                </c:forEach>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                    <div class="card-footer text-muted">
                        <div class="row">
                            <div class="col text-left">
                                <button type="button" class="btn btn-dark btn-sm m-1"
                                        data-role="question-carousel-prev">
                                    <
                                </button>
                            </div>
                            <div class="col text-center">
                                <button class="btn btn-dark m-1" type="submit" name="btnResult" data-role="indexing"
                                        formaction="${pageContext.request.contextPath}/testboard/result">
                                    Result
                                </button>
                            </div>
                            <div class="col text-right">
                                <button type="button" class="btn btn-dark btn-sm m-1"
                                        data-role="question-carousel-next">
                                    >
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<%@ include file="JSlibs.jsp" %>
<script src="${pageContext.request.contextPath}/resources/js/RunTest.js"></script>
<%@ include file="Footer.jsp" %>
