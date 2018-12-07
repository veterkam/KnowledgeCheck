<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file = "../common/Header.jsp" %>
<%@ include file = "../common/NavBar.jsp" %>

<div class="container text-left">
    <h1 class="h3 mb-3 font-weight-normal">Test list</h1>
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
                                <strong>Subject</strong>: <c:out value="${test.subject.name}"/>
                            </c:if>
                        </div>
                        <div class="col text-right">
                            <c:if test="${test.tutor != null}">
                                <strong>Author</strong>:
                                <a href="${pageContext.request.contextPath}/authorization/profile?id=${test.tutor.id}">
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
                            <p class="card-text small">Update: <fmt:formatDate type = "date" value = "${test.updateTime}" /></p>
                        </div>
                        <c:if test="${user != null && user.role=='STUDENT'}">
                            <c:if test="${scores[loop.index] >= 0}">
                                <div class="col text-left">
                                    <p class="card-text small">My result: ${scores[loop.index]}%</p>
                                </div>
                            </c:if>
                            <div class="col text-right">
                                <a href="${pageContext.request.contextPath}/testing/testing?testId=${test.id}"
                                   class="btn btn-sm btn-dark m-1" role="button">
                                    Run testing
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
</div>
<%@ include file="../common/JSlibs.jsp" %>
<%@ include file="../common/Footer.jsp" %>
