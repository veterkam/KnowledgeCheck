<%@ include file="../common/Header.jsp" %>
    <h1 class="h3 mb-3 font-weight-normal"><fmt:message key="app.account.profile"/></h1>
    <%@ include file = "../common/Alert.jsp" %>
    <p><strong><fmt:message key="${userData.role.caption}"/> </strong> <c:out value="${userData.fullname}"/></p>
    <p><strong><fmt:message key="app.account.username"/>:</strong> <c:out value="${userData.username}"/></p>
    <p><strong><fmt:message key="app.account.email"/>:</strong> <c:out value="${userData.email}"/></p>
    <c:if test="${userData.role == 'STUDENT'}">
        <p><strong><fmt:message key="app.account.specialty"/>:</strong> <c:out value="${userData.specialty}"/></p>
        <p><strong><fmt:message key="app.account.group"/>:</strong> <c:out value="${userData.group}"/></p>
        <p><strong><fmt:message key="app.account.year"/>:</strong> <c:out value="${userData.year}"/></p>
    </c:if>
    <c:if test="${userData.role == 'TUTOR'}">
        <p><strong><fmt:message key="app.account.position"/>:</strong> <c:out value="${userData.position}"/></p>
        <p><strong><fmt:message key="app.account.scientific_degree"/>:</strong> <c:out value="${userData.scientificDegree}"/></p>
        <p><strong><fmt:message key="app.account.academic_title"/>:</strong> <c:out value="${userData.academicTitle}"/></p>
    </c:if>
<%@ include file="../common/JSlibs.jsp" %>
<script src="${pageContext.request.contextPath}/resources/js/MyProfile.js"></script>
<%@ include file="../common/Footer.jsp" %>
