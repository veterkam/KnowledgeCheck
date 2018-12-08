<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${alertManager != null}">
    <c:forEach var="alert" items="${alertManager.alerts}">
        <div class="alert alert-${alert.type} alert-dismissible fade show">
            <button type="button" class="close" data-dismiss="alert">&times;</button>
            <strong class="text-capitalize">${alert.type}:</strong> <fmt:message key="${alert.message}" />
        </div>
    </c:forEach>
    ${alertManager.clear}
</c:if>

