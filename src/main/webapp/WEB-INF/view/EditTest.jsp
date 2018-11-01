<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="Header.jsp" %>
<%@ include file = "NavBar.jsp" %>
<div class="container">
    <h1 class="h3 mb-3 font-weight-normal">Edit test</h1>
    <%@ include file = "Alert.jsp" %>
    <form class="form-text-edit text-left" method="post">
        <div class="form-group">
            <label for="inputTitle">Title:</label>
            <input id="inputTitle" class="form-control" name="title"
                   placeholder="Enter title of test" required="" autofocus="" type="text"
                   value="<c:out value="${test.title}"/>">
        </div>
        <div class="form-group">
            <label for="selectSubject" >Subject:</label>
            <select id="selectSubject" name="subject" class="custom-select form-control" required="" >
                <option disabled>Choose a subject</option>
                <c:forEach var="subject" items="${subjects}">
                    <option <c:if test="${subject.id == test.subjectId}">selected</c:if>  value="${subject.id}">
                        <c:out value="${subject.name}"/>
                    </option>
                </c:forEach>
            </select>
        </div>
        <div class="form-group">
            <label for="inputDescription">Description:</label>
            <textarea class="form-control" rows="5" id="inputDescription" name="description"
                placeholder="Enter description of test" autofocus=""
                value="<c:out value="${test.description}"/>">
            </textarea>
        </div>


        <div class="card mb-2">
            <div class="card-body">
                <div class="form-group">
                    <label for="inputQuestion">Question:</label>
                    <textarea class="form-control" rows="5" id="inputQuestion" name="question" required=""
                              placeholder="Enter question" autofocus="">
                    </textarea>
                    <div class="input-group">
                        <input id="inputAnswer" class="form-control" name="answer"
                               placeholder="Enter answer of the question" required="" autofocus="" type="text">
                        <div class="input-group-append">
                            <div class="input-group-text">
                                <input type="checkbox" title="Is the answer correct? Check if Yes.">
                            </div>
                            <button class="btn btn-success" type="button" title="Add answer">+</button>
                            <button class="btn btn-danger" type="button" title="Delete answer">-</button>
                        </div>

                    </div>

                    <div class="form-group text-right">
                        <button class="btn btn-dark mr-2 mt-4" type="button">Add question</button>
                        <button class="btn btn-dark mt-4" type="button">Delete question</button>
                    </div>
                </div>
            </div>
        </div>



        <div class="form-group text-right">
            <button class="btn btn-dark m-1" type="submit" name="btnSave"
                    formaction="${pageContext.request.contextPath}/testboard/add">
                Save
            </button>
        </div>

    </form>
</div>
<%@ include file="Footer.jsp" %>
