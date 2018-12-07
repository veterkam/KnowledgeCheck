<c:if test="${pagination != null && pagination.pageCount > 0}">
    <ul class="pagination justify-content-center">
        <li class="page-item">
            <a class="page-link" href="?pageNo=${pagination.prev}">
                Previous
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
                Next
            </a>
        </li>
    </ul>
</c:if>