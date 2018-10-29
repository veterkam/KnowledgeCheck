<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${attentionMessage != null}">
    <div class="alert alert-warning alert-dismissible fade show">${attentionMessage}</div>
</c:if>
