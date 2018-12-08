<c:if test="${pagination != null && pagination.pageCount > 0}">
    <ul class="pagination justify-content-center">
        <li class="page-item">
            <a class="page-link" href="?pageNo=${pagination.prev}">
                <fmt:message key="app.common.back" />
            </a>
        </li>
        <c:forEach begin="${pagination.begin}" end="${pagination.end}" varStatus="loop">
            <li class='page-item <c:if test="${loop.index == pagination.current}"> active </c:if> '>
                <a class="page-link" href="?pageNo=${loop.index}">
                        ${loop.index}
                </a>
            </li>
        </c:forEach>
        <li class="page-item">
            <a class="page-link" href="?pageNo=${pagination.next}">
                <fmt:message key="app.common.next" />
            </a>
        </li>
    </ul>
</c:if>