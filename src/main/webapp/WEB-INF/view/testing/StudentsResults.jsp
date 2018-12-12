<%@ include file = "../common/Header.jsp" %>
    <div class="row">
        <div class="col">
            <h1 class="h3 mb-3 font-weight-normal"><fmt:message key="app.testing.students_results" /></h1>
        </div>
    </div>

    <%@ include file="../common/Alert.jsp" %>

    <c:if test="${fn:length(tests) == 0}">
        <div class="row">
            <div class="col">
                <h3 class="h5 mb-3 font-weight-normal">
                    <fmt:message key="app.testing.you_do_not_have_any_test" />&nbsp;<a href="${pageContext.request.contextPath}/testing/edit"><fmt:message key="app.testing.new_test" /></a>
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
                                        <strong><fmt:message key="app.testing.subject" /></strong>: <c:out value="${test.subject.name}"/>
                                    </c:if>
                                </div>
                                <div class="col text-right">
                                    <c:if test="${test.tutor != null}">
                                        <strong><fmt:message key="app.testing.author" /></strong>: <c:out value="${test.tutor.firstName} ${test.tutor.lastName}"/>
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
                                                            <a href="${pageContext.request.contextPath}/account/profile/${studentTestingResults.student.username}">
                                                                <c:out value="${studentTestingResults.student.fullname}"/></a>
                                                        </strong>
                                                        ${studentTestingResults.score}% <fmt:message key="app.testing.correct_answers" />
                                                    </p>
                                                </div>
                                                <div class="col text-right">
                                                    <button type="button" class="btn btn-dark btn-sm m-1" data-toggle="collapse"
                                                            data-target="#testingResults${testLoop.index}_${studentTestingResultsLoop.index}">
                                                        <fmt:message key="app.testing.results" />
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
                                        <fmt:message key="app.common.details" />
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
<%@ include file="../common/JSlibs.jsp" %>
<%@ include file="../common/Footer.jsp" %>
