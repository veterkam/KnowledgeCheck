<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file = "../common/Header.jsp" %>
<%@ include file = "../common/NavBar.jsp" %>
<div class="container text-left">

    <div class="row">
        <div class="col">
            <h1 class="h3 mb-3 font-weight-normal">Subjects</h1>
        </div>
    </div>

    <%@ include file="../common/Alert.jsp" %>
    <form class="col-12" method="post">
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
                                               placeholder="Enter subject name" required="" autofocus="" type="text"
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
                    <button class="btn btn-dark m-1 mt-4" data-role="add-subject">Add subject</button>
                    <button class="btn btn-dark m-1 mt-4" type="submit"
                            formaction="<c:url value="/testboard/subjects/save"/>">
                        Save changes
                    </button>
                    <a class="btn btn-dark m-1 mt-4" role="button"
                       href="<c:url value="/testboard/subjects"/>" >
                        Cancel
                    </a>
                </div>

            </div>
        </div>
    </form>
</div>
<%@ include file="../common/JSlibs.jsp" %>
<script src="<c:url value="/resources/js/Subjects.js"/>"></script>
<%@ include file="../common/Footer.jsp" %>
