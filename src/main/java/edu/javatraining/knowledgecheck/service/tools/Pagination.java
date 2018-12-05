package edu.javatraining.knowledgecheck.service.tools;

public class Pagination {
    private int begin;
    private int end;
    private int current;
    private int prev;
    private int next;
    private int pageCount;
    private int elementLimit;

    public Pagination(int pageNo, int pageCount, int pageLimit) {
        this.current = pageNo;
        this.pageCount = pageCount;

        int half = (pageLimit % 2 == 0) ? pageLimit / 2 : pageLimit / 2 + 1;

        if(current < half ) {
            begin = 1;
            end = Math.min(pageCount, pageLimit);
        } else if(current > pageCount - half) {
            begin = Math.max(pageCount - pageLimit + 1, 1);
            end = pageCount;
        } else {
            begin = current - half + 1;
            end = begin + pageLimit - 1;
        }

        prev = Math.max(current - 1, 1);
        next = Math.min(current + 1, pageCount);
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getPrev() {
        return prev;
    }

    public void setPrev(int prev) {
        this.prev = prev;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public int getPageCount() {
        return pageCount;
    }

    public int getElementLimit() {
        return elementLimit;
    }

    public void setElementLimit(int elementLimit) {
        this.elementLimit = elementLimit;
    }
}
