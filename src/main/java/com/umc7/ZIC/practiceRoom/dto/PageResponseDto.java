package com.umc7.ZIC.practiceRoom.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageResponseDto<T>(
        List<T> resultList,   // 실제 데이터 목록
        Integer listSize,    // 현재 페이지의 데이터 개수
        Integer totalPage,  // 전체 페이지 수
        Long totalElements,  // 전체 요소 개수 (모든페이지를 합한수)
        Boolean isFirst,      // 현재 페이지가 첫 페이지인지 여부
        Boolean isLast       // 현재 페이지가 마지막 페이지인지 여부
) {

    public static <T> PageResponseDto<T> from(Page<T> page) {
        return new PageResponseDto<>(
                page.getContent(),      // 데이터 목록
                page.getNumberOfElements(), // 현재 페이지의 요소 개수
                page.getTotalPages(),       // 전체 페이지 수
                page.getTotalElements(),    // 전체 요소 개수
                page.isFirst(),         // 현재 페이지가 첫 페이지인지 여부
                page.isLast()          // 현재 페이지가 마지막 페이지인지 여부
        );
    }
}