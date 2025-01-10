package com.apibuilder.dev.apibuilder.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;
import java.util.List;

import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PageResponse<T> {
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;

    public void setPageStats(Page<T> page) {
        this.content = page.getContent();
        this.pageNumber = page.getNumber();
        this.pageSize = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.last = page.isLast();
    }
}