<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" isErrorPage="true"%>
<%@ page import="java.io.StringWriter" %>
<%@ page import="java.io.PrintWriter" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file = "../common/Header.jsp" %>
<%@ include file = "../common/NavBar.jsp" %>
<div class="container text-left">
    <h1 class="h3 mb-3 font-weight-normal"><fmt:message key="app.common.error" /></h1>
    <h5><fmt:message key="app.common.error_occurred" /></h5>
    <p>
    <%
        StringWriter sw = new StringWriter();
        exception.printStackTrace(new PrintWriter(sw));
        out.print(sw.toString());
    %>
    </p>
</div>
<%@ include file="../common/JSlibs.jsp" %>
<%@ include file="../common/Footer.jsp" %>

