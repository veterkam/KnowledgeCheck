<%@ include file = "../common/Header.jsp" %>

    <div class="row">
        <div class="col">
            <h1 class="h3 mb-3 font-weight-normal"><fmt:message key="app.testing.subjects" /></h1>
        </div>
    </div>

    <form class="col-12" method="post">
        <div class="form-group">
            <%@ include file = "../common/Alert.jsp" %>
        </div>
        <div class="row">
            <div class="col">
                <div class="table-wrapper-scroll-y">
                    <table class="table table-bordered" data-role="subject-list-container">
                        <tbody>
                        <c:forEach var="subject" items="${subjects}" varStatus="subjectLoop">
                            <tr data-role="subject-container">
                                <th data-role="num-container" class="paddingl0 paddingb0 text-center" scope="row">
                                    ${subjectLoop.count}
                                </th>
                                <td class="padding0">
                                    <div class="input-group mt-1" >
                                        <input hidden type="text" data-role="subject-id" name="subjectIds" value="${subject.id}">
                                        <input hidden type="text" data-role="subject-remove-flag" name="remove" value="0">
                                        <input hidden type="text" data-role="subject-modify-flag" name="modify" value="0">

                                        <input data-role="subject-input" class="form-control" name="subjects"
                                               placeholder='<fmt:message key="app.testing.enter_subject_name"/>' required="" autofocus="" type="text"
                                               value="<c:out value="${subject.name}"/>">
                                        <div class="input-group-append">
                                            <button class="btn btn-dark" type="button" title="Remove subject" data-role="remove-subject">-</button>
                                        </div>
                                    </div>

                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>

                <div class="text-right">
                    <button class="btn btn-dark m-1 mt-4" data-role="add-subject"><fmt:message key="app.testing.add_subject" /></button>
                    <button class="btn btn-dark m-1 mt-4" type="submit"
                            formaction="${pageContext.request.contextPath}/testing/subjects">
                        <fmt:message key="app.common.save_changes" />
                    </button>
                    <a class="btn btn-dark m-1 mt-4" role="button"
                       href="${pageContext.request.contextPath}/testing/subjects" >
                        <fmt:message key="app.common.cancel" />
                    </a>
                </div>

            </div>
        </div>
        <div class="form-row">
            <input hidden name="_FID" type="text" value="${FID}">
        </div>
    </form>
<%@ include file="../common/JSlibs.jsp" %>
<script src="${pageContext.request.contextPath}/resources/js/Subjects.js"></script>
<%@ include file="../common/Footer.jsp" %>
