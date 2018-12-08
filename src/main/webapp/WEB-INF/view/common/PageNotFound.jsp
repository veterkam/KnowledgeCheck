<%@ include file = "Header.jsp" %>
<%@ include file = "NavBar.jsp" %>
<div class="container">
    <h1 class="h3 mb-3 font-weight-normal text-center"><fmt:message key="app.common.page_not_found" /></h1>
    <%@ include file = "Alert.jsp" %>
    <p class="text-center mt-5">
        <fmt:message key="app.common.please_start_with" /> <a href="<c:url value="/"/>"><fmt:message key="app.common.home_page" /></a>.

    </p>
</div>
<%@ include file="JSlibs.jsp" %>
<%@ include file="Footer.jsp" %>
