package com.umc7.ZIC.practiceRoom.dto;

import org.springframework.data.domain.Page;

public record PageResponseDto(
        Integer totalPages, // 전체 페이지 수
        Integer currentPage, // 현재 페이지 번호 (1부터 시작)
        Long totalElements, // 전체 요소 개수
        Integer currentSize // 현재 페이지의 요소 개수
) {

    public static PageResponseDto from(Page<?> page) {
        return new PageResponseDto(
                page.getTotalPages(),
                page.getNumber() + 1, // 0-based index이므로 1을 더함
                page.getTotalElements(),
                page.getNumberOfElements()
        );
    }
}