<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="../common/Header.jsp" %>
<%@ include file = "../common/NavBar.jsp" %>
<div class="container text-left">
    <h1 class="h3 mb-3 font-weight-normal">Profile</h1>
    <%@ include file = "../common/Alert.jsp" %>
    <p><strong><c:out value="${userData.role}"/> </strong> <c:out value="${userData.fullname}"/></p>
    <%--<p><strong>E-mail address:</strong> <c:out value="${userData.email}"/></p>--%>
    <c:if test="${userData.role == 'STUDENT'}">
        <p><strong>Specialty:</strong> <c:out value="${userData.specialty}"/></p>
        <p><strong>Group:</strong> <c:out value="${userData.group}"/></p>
        <p><strong>Year:</strong> <c:out value="${userData.year}"/></p>
    </c:if>
    <c:if test="${userData.role == 'TUTOR'}">
        <p><strong>Position:</strong> <c:out value="${userData.position}"/></p>
        <p><strong>Scientific degree:</strong> <c:out value="${userData.scientificDegree}"/></p>
        <p><strong>Academic title:</strong> <c:out value="${userData.academicTitle}"/></p>
    </c:if>
</div>
<%@ include file="../common/JSlibs.jsp" %>
<script src="${pageContext.request.contextPath}/resources/js/MyProfile.js"></script>
<%@ include file="../common/Footer.jsp" %>
