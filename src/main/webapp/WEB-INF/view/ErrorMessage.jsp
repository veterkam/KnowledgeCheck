<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${errorMessage != null}">
    <div style="background-color: orangered; width: 60%; margin: 10px;">${errorMessage}</div>
</c:if>