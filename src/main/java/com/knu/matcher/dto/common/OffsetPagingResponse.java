package com.knu.matcher.dto.common;

import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public class OffsetPagingResponse<T> {
    private boolean hasNext;
    private final List<T> data = new ArrayList<>();
}
