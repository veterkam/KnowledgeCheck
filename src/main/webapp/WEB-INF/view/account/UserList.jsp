<%@ include file = "../common/Header.jsp" %>

    <div class="row">
        <div class="col">
            <h1 class="h3 mb-3 font-weight-normal"><fmt:message key="app.account.users"/></h1>
        </div>
    </div>

    <div class="row">
        <div class="col">
            <%@ include file="../common/Alert.jsp" %>
        </div>
    </div>

    <c:if test="${fn:length(users) > 0}">
        <div class="row">
            <div class="col">
                <%@ include file="../common/Pagination.jsp" %>
            </div>
        </div>
        <c:set value="${pagination.elementLimit * (pagination.current - 1)}" var="offset"/>
        <c:forEach var="userData" items="${users}" varStatus="userLoop">
            <div class="card mt-1">
                <div class="card-header">
                    <div class="row">
                        <div class="col text-left">
                            <p class="card-text">
                                <strong>${offset + userLoop.count}. <c:out value="${userData.username}"/> </strong>
                                <c:out value="${userData.fullname}"/>
                            </p>
                        </div>
                        <div class="col text-right">
                            <c:if test="${userData.verified == true}">
                                <a href="${pageContext.request.contextPath}/account/users/verify?username=${userData.username}&verified=false"
                                   class="btn btn-sm btn-dark m-1" role="button">
                                    <fmt:message key="app.account.return_to_the_verification"/>
                                </a>
                            </c:if>
                            <c:if test="${userData.verified == false}">
                                <a href="${pageContext.request.contextPath}/account/users/verify?username=${userData.username}&verified=true"
                                   class="btn btn-sm btn-dark m-1" role="button">
                                    <fmt:message key="app.account.verification_passed"/>
                                </a>
                            </c:if>
                            <a href="${pageContext.request.contextPath}/account/users/remove?username=${userData.username}"
                               class="btn btn-sm btn-dark m-1" role="button" onclick="return confirm('<fmt:message key="app.account.user_removing_confirmation"/>')">
                                <fmt:message key="app.common.remove"/>
                            </a>
                            <button type="button" class="btn btn-dark btn-sm m-1" data-toggle="collapse"
                                    data-target="#userData${userLoop.index}">
                                <fmt:message key="app.common.details"/>
                            </button>

                        </div>
                    </div>
                </div>
                <div class="card-body collapse" id="userData${userLoop.index}">
                    <div class="row">
                        <div class="col-sm-3">
                            <strong><fmt:message key="app.account.full_name"/>: </strong>
                        </div>
                        <div class="col-sm-9">
                            <c:out value="${userData.fullname}"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-3">
                            <strong><fmt:message key="app.account.username"/>: </strong>
                        </div>
                        <div class="col-sm-9">
                            <c:out value="${userData.username}"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-3">
                            <strong><fmt:message key="app.account.email"/>: </strong>
                        </div>
                        <div class="col-sm-9">
                            <c:out value="${userData.email}"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-3">
                            <strong><fmt:message key="app.account.role"/>: </strong>
                        </div>
                        <div class="col-sm-9">
                            <fmt:message key="${userData.role.caption}"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-3">
                            <strong><fmt:message key="app.account.verified"/>: </strong>
                        </div>
                        <div class="col-sm-9">
                            <c:if test="${userData.verified == true}">&#x2714;</c:if>
                            <c:if test="${userData.verified == false}">&#x2718;</c:if>
                        </div>
                    </div>

                    <c:if test="${userData.role=='TUTOR'}">
                        <div class="row">
                            <div class="col-sm-3">
                                <strong><fmt:message key="app.account.position"/>: </strong>
                            </div>
                            <div class="col-sm-9">
                                <c:out value="${userData.position}"/>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-3">
                                <strong><fmt:message key="app.account.scientific_degree"/>: </strong>
                            </div>
                            <div class="col-sm-9">
                                <c:out value="${userData.scientificDegree}"/>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-3">
                                <strong><fmt:message key="app.account.academic_title"/>: </strong>
                            </div>
                            <div class="col-sm-9">
                                <c:out value="${userData.academicTitle}"/>
                            </div>
                        </div>
                    </c:if>

                    <c:if test="${userData.role=='STUDENT'}">
                        <div class="row">
                            <div class="col-sm-3">
                                <strong><fmt:message key="app.account.specialty"/>: </strong>
                            </div>
                            <div class="col-sm-9">
                                <c:out value="${userData.specialty}"/>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-3">
                                <strong><fmt:message key="app.account.group"/>: </strong>
                            </div>
                            <div class="col-sm-9">
                                <c:out value="${userData.group}"/>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-3">
                                <strong><fmt:message key="app.account.year"/>: </strong>
                            </div>
                            <div class="col-sm-9">
                                <c:out value="${userData.year}"/>
                            </div>
                        </div>
                    </c:if>
                </div>
            </div>
        </c:forEach>

        <div class="row">
            <div class="col">
                <%@ include file="../common/Pagination.jsp" %>
            </div>
        </div>
    </c:if>
<%@ include file="../common/JSlibs.jsp" %>
<%@ include file="../common/Footer.jsp" %>
