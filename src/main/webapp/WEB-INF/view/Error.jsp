<%@ page import="java.io.StringWriter" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isErrorPage="true" %>
<html>
<head>
    <meta charset="utf-8">
    <title>Error</title>
</head>
<body>
    <h1 align="center" >Error</h1>
    <div align="left">
        <p>
            <%
                StringWriter sw = new StringWriter();
                exception.printStackTrace(new PrintWriter(sw));
                out.print(sw.toString());
            %>
        </p>
    </div>
</body>
</html>
