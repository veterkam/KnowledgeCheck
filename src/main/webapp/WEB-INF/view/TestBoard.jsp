<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file = "Header.jsp" %>
<%@ include file = "NavBar.jsp" %>
<div class="container text-left">
    <h1 class="h3 mb-3 font-weight-normal">Latest tests</h1>
    <%@ include file="Alert.jsp" %>
    <c:forEach var="test" items="${tests}" varStatus="loop">
        <c:if test="${loop.index % 2 == 0}"><div class="row"></c:if>
        <div class="col-sm-6">
            <div class="card mb-4">
                <div class="card-header">
                    <div class="row">
                        <div class="col text-left">
                            <c:if test="${test.subject != null}">
                                <strong>Subject</strong>: ${test.subject.name}
                            </c:if>
                        </div>
                        <div class="col text-right">
                            <c:if test="${test.tutor != null}">
                                <strong>Author</strong>: ${test.tutor.firstname} ${test.tutor.lastname}
                            </c:if>
                        </div>
                    </div>
                </div>
                <div class="card-body">
                    <h5 class="card-title">${test.title}</h5>
                    <div id="desc${loop.index}" class="collapse">
                        <p class="card-text">${test.description}</p>
                    </div>
                </div>
                <div class="card-footer text-muted">
                    <div class="row">
                        <div class="col text-left">
                            <fmt:formatDate type = "both" timeStyle = "short" value = "${test.updateTime}" />
                        </div>
                        <div class="col text-right">
                            <button type="button" class="btn btn-dark btn-sm" data-toggle="collapse" data-target="#desc${loop.index}">
                                Description
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <c:if test="${loop.index % 2 == 1 || loop.last}"></div></c:if>
    </c:forEach>
    <div class="row">
        <div class="col">
            <ul class="pagination justify-content-center">
                <li class="page-item">
                    <a class="page-link" href="${pageContext.request.contextPath}?pageNo=${pagination.prev}">
                        Previous
                    </a>
                </li>
                <c:forEach begin="${pagination.begin}" end="${pagination.end}" varStatus="loop">
                    <li class='page-item <c:if test="${loop.index == pagination.current}"> active </c:if> '>
                        <a class="page-link" href="${pageContext.request.contextPath}?pageNo=${loop.index}">
                                ${loop.index}
                        </a>
                    </li>
                </c:forEach>
                <li class="page-item">
                    <a class="page-link" href="${pageContext.request.contextPath}?pageNo=${pagination.next}">
                        Next
                    </a>
                </li>
            </ul>
        </div>
    </div>
</div>
<%@ include file="Footer.jsp" %>
