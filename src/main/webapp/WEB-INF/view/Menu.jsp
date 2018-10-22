<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${user != null}">
    <div align="right" style="background-color: orangered">
        <span>Welcome <c:out value="${user.username}"/>!</span>
        <span><a href="${pageContext.request.contextPath}/sendmail">Send e-mail</a></span>
        <span><a href="${pageContext.request.contextPath}/logout">Logout</a></span>
    </div>
</c:if>
