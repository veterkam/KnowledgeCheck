<%@ include file = "../common/Header.jsp" %>
    <h1 class="h3 mb-3 font-weight-normal"><fmt:message key="app.testing.test_list" /></h1>
    <%@ include file="../common/Alert.jsp" %>
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
    <c:forEach var="test" items="${tests}" varStatus="loop">
        <c:if test="${loop.index % 2 == 0}"><div class="row"></c:if>
        <div class="col-sm-6">
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
                                <strong><fmt:message key="app.testing.author" /></strong>:
                                <a href="${pageContext.request.contextPath}/account/profile/${test.tutor.username}">
                                    <c:out value="${test.tutor.fullname}"/>
                                </a>
                            </c:if>
                        </div>
                    </div>
                </div>
                <div class="card-body">
                    <h5 class="card-title"><c:out value="${test.title}"/></h5>
                    <p class="card-text"><c:out value="${test.description}"/></p>
                </div>
                <div class="card-footer text-muted">
                    <div class="row">
                        <div class="col text-left">
                            <p class="card-text small"><fmt:message key="app.testing.update" />: <fmt:formatDate type = "date" value = "${test.updateTime}" /></p>
                        </div>
                        <c:if test="${user != null && user.role=='STUDENT'}">
                            <c:if test="${scores[loop.index] >= 0}">
                                <div class="col text-left">
                                    <p class="card-text small"><fmt:message key="app.testing.my_result" />: ${scores[loop.index]}%</p>
                                </div>
                            </c:if>
                            <div class="col text-right">
                                <a href="${pageContext.request.contextPath}/testing/test?id=${test.id}"
                                   class="btn btn-sm btn-dark m-1" role="button">
                                    <fmt:message key="app.testing.run_testing" />
                                </a>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
        <c:if test="${loop.index % 2 == 1 || loop.last}"></div></c:if>
    </c:forEach>
    <div class="row">
        <div class="col">
            <%@ include file="../common/Pagination.jsp" %>
        </div>
    </div>
<%@ include file="../common/JSlibs.jsp" %>
<%@ include file="../common/Footer.jsp" %>
