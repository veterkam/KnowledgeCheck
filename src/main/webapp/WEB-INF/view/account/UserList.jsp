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
            <h1 class="h3 mb-3 font-weight-normal">Users</h1>
        </div>
    </div>

    <%@ include file="../common/Alert.jsp" %>

    <c:if test="${fn:length(users) > 0}">
        <div class="row">
            <div class="col">
                <%@ include file="../common/Pagination.jsp" %>
            </div>
        </div>
        <c:set value="${pagination.elementLimit * (pagination.current - 1)}" var="offset"/>
        <c:forEach var="userData" items="${users}" varStatus="userLoop">
            <div class="card mt-1">
                <div class="card-header">
                    <div class="row">
                        <div class="col text-left">
                            <p class="card-text">
                                <strong>${offset + userLoop.count}. <c:out value="${userData.username}"/> </strong>
                                <c:out value="${userData.fullname}"/>
                            </p>
                        </div>
                        <div class="col text-right">
                            <c:if test="${userData.verified == true}">
                                <a href="<c:url value="/authorization/users/verify?id=${userData.id}&verified=false"/>"
                                   class="btn btn-sm btn-dark m-1" role="button">
                                    Return to the verification
                                </a>
                            </c:if>
                            <c:if test="${userData.verified == false}">
                                <a href="<c:url value="/authorization/users/verify?id=${userData.id}&verified=true"/>"
                                   class="btn btn-sm btn-dark m-1" role="button">
                                    Verification passed
                                </a>
                            </c:if>
                            <a href="<c:url value="/authorization/users/remove?id=${userData.id}"/>"
                               class="btn btn-sm btn-dark m-1" role="button" onclick="return confirm('Are you sure you want to remove the user?')">
                                Remove
                            </a>
                            <button type="button" class="btn btn-dark btn-sm m-1" data-toggle="collapse"
                                    data-target="#userData${userLoop.index}">
                                Details
                            </button>

                        </div>
                    </div>
                </div>
                <div class="card-body collapse" id="userData${userLoop.index}">
                    <div class="row">
                        <div class="col-sm-3">
                            <strong>Full name: </strong>
                        </div>
                        <div class="col-sm-9">
                            <c:out value="${userData.fullname}"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-3">
                            <strong>Username: </strong>
                        </div>
                        <div class="col-sm-9">
                            <c:out value="${userData.username}"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-3">
                            <strong>E-mail: </strong>
                        </div>
                        <div class="col-sm-9">
                            <c:out value="${userData.email}"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-3">
                            <strong>Role: </strong>
                        </div>
                        <div class="col-sm-9">
                            <c:out value="${userData.role}"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-3">
                            <strong>Verified: </strong>
                        </div>
                        <div class="col-sm-9">
                            <c:if test="${userData.verified == true}">&#x2714;</c:if>
                            <c:if test="${userData.verified == false}">&#x2718;</c:if>
                        </div>
                    </div>

                    <c:if test="${userData.role=='TUTOR'}">
                        <div class="row">
                            <div class="col-sm-3">
                                <strong>Position: </strong>
                            </div>
                            <div class="col-sm-9">
                                <c:out value="${userData.position}"/>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-3">
                                <strong>Scientific degree: </strong>
                            </div>
                            <div class="col-sm-9">
                                <c:out value="${userData.scientificDegree}"/>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-3">
                                <strong>Academic title: </strong>
                            </div>
                            <div class="col-sm-9">
                                <c:out value="${userData.academicTitle}"/>
                            </div>
                        </div>
                    </c:if>

                    <c:if test="${userData.role=='STUDENT'}">
                        <div class="row">
                            <div class="col-sm-3">
                                <strong>Specialty: </strong>
                            </div>
                            <div class="col-sm-9">
                                <c:out value="${userData.specialty}"/>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-3">
                                <strong>Group: </strong>
                            </div>
                            <div class="col-sm-9">
                                <c:out value="${userData.group}"/>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-3">
                                <strong>Year: </strong>
                            </div>
                            <div class="col-sm-9">
                                <c:out value="${userData.year}"/>
                            </div>
                        </div>
                    </c:if>
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
