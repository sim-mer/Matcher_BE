package com.knu.matcher.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@Getter
public class OffsetPagingResponse<T> {
    private Boolean hasNext;
    private List<T> data;
}
