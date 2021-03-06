<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${alertManager != null}">
    <c:forEach var="alert" items="${alertManager.alerts}">
        <div class="alert alert-${alert.type} alert-dismissible fade show">
            <button type="button" class="close" data-dismiss="alert">&times;</button>
            <strong class="text-capitalize"><fmt:message key="app.alert.${alert.type}"/>:</strong>
            <c:if test='${!alert.isKeyi18n}'><c:out value="${alert.message}" /></c:if>
            <c:if test='${alert.isKeyi18n}'><fmt:message key="${alert.message}" /></c:if>
        </div>
    </c:forEach>
    ${alertManager.clear}
</c:if>

