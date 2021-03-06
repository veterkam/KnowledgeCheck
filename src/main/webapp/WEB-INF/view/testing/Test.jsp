<%@ include file = "../common/Header.jsp" %>
    <div class="row">
        <div class="col">
            <h1 class="h3 mb-3 font-weight-normal"><fmt:message key="app.testing" /></h1>
        </div>
        <div class="col text-right">
            <h1 hidden class="h3 mb-3 font-weight-normal text-danger" data-role="test-remaining-time"><c:out value="${test.timeLimitation}"/></h1>
        </div>
    </div>

    <div class="row">
        <%@ include file="../common/Alert.jsp" %>
    </div>
    <div class="row">
        <div class="col">
            <form class="form-text-edit text-left" data-role="test-container" id="test-carousel-container" method="post">
                <input type="text" name="testId" value="${test.id}" hidden>
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
                                    <strong><fmt:message key="app.testing.author" /></strong>: <c:out value="${test.tutor.fullname}"/>
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
                                        formaction="${pageContext.request.contextPath}/testing/test">
                                    <fmt:message key="app.testing.result" />
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
                <div class="form-group">
                    <input hidden name="_FID" type="text" value="${FID}">
                </div>
            </form>
        </div>
    </div>
<%@ include file="../common/JSlibs.jsp" %>
<script src="${pageContext.request.contextPath}/resources/js/RunTest.js"></script>
<%@ include file="../common/Footer.jsp" %>
