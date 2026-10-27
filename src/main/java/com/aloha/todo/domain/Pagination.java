package com.aloha.todo.domain;

import lombok.Data;

/**
 * 페이징 정보
 * - 필수 : page(현재 페이지), size(페이지당 개수), count(노출 페이지 수), total(전체 개수)
 * - 계산 : start/end(노출 페이지 범위), first/last(첫/마지막 페이지), prev/next, index(시작 순번)
 */
@Data
public class Pagination {
    private static final long PAGE_NUM = 1;
    private static final long SIZES = 10;
    private static final long COUNT = 10;

    private long page;
    private long size;
    private long count;
    private long total;

    private long start;
    private long end;
    private long first;
    private long last;
    private long prev;
    private long next;
    private long index;

    public Pagination() {
        this(PAGE_NUM, SIZES, COUNT, 0);
    }

    public Pagination(long page, long size, long count, long total) {
        this.page = page;
        this.size = size;
        this.count = count;
        this.total = total;
        calc();
    }

    // 전체 개수를 지정하면 페이지 정보를 다시 계산한다.
    public void setTotal(long total) {
        this.total = total;
        calc();
    }

    public void calc() {
        this.first = 1;
        this.last = Math.max(1, (total - 1) / size + 1);
        this.start = ((page - 1) / count) * count + 1;
        this.end = Math.min(((page - 1) / count + 1) * count, last);
        this.prev = page - 1;
        this.next = page + 1;
        this.index = (page - 1) * size;
    }
}
