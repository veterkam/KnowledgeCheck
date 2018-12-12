<%@ include file = "../common/Header.jsp" %>
    <div class="row">
        <div class="col">
            <h1 class="h3 mb-3 font-weight-normal"><fmt:message key="app.testing.test_statistics" /></h1>
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
                            <p class="card-text">${statisticsList[testLoop.index].studentCount} <fmt:message key="app.testing.students_passed_the_test" /></p>
                            <div id="desc${testLoop.index}" class="collapse">
                                <c:forEach var="question" items="${test.questions}" varStatus="questionLoop">
                                    <div class="row">
                                        <div class="col">
                                            ${questionLoop.count}. <c:out value="${question.description}"/>
                                        </div>
                                    </div>
                                    <c:if test="${statisticsList[testLoop.index].studentCount > 0}">
                                        <div class="row">
                                            <div class="col">
                                                <div class="progress mb-4">
                                                    <div id="test-progress-bar"
                                                         class="progress-bar bg-dark progress-bar-striped text-center"
                                                         style="width:${statisticsList[testLoop.index].scores[question.id]}%">
                                                        ${statisticsList[testLoop.index].scores[question.id]}% <fmt:message key="app.testing.correct_answers" />
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </c:if>
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
                                            data-target="#desc${testLoop.index}">
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
