package com.umc7.ZIC.practiceRoom.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@ParameterObject
public record PageRequestDto(
        @Parameter(description = "페이지 번호 (1 이상)")
        @Min(value = 1, message = "페이지 번호는 1 이상이어야 합니다.")
        int page,

        @Parameter(description = "페이지 크기 (10-100 사이)")
        @Min(value = 10)
        @Max(value = 100)
        int size,

        @Parameter(description = "가격 정렬 (asc 또는 desc, 대소문자 구분 없음), 아무값이나 넣으면 id ASC 정렬")
        String priceSort // 가격 정렬
) {

    public Pageable toPageable(Sort sort) {
        // page는 0부터 시작하므로 1을 빼줌
        return org.springframework.data.domain.PageRequest.of(page - 1, size, sort);
    }

    public Pageable toPageable() {
        return org.springframework.data.domain.PageRequest.of(page - 1, size, Sort.by("id").ascending());
    }
}