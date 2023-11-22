package com.knu.matcher.domain.jobpost;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JobPostImg {
    private long id;
    private String name;
    private long jobPostId;
    private String url;
}
