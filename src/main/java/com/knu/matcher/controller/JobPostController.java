package com.knu.matcher.controller;

import com.knu.matcher.annotation.TokenUserEmail;
import com.knu.matcher.dto.common.OffsetPagingResponse;
import com.knu.matcher.dto.jobpost.*;
import com.knu.matcher.service.JobPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jobpost")
public class JobPostController {
    private final JobPostService jobPostService;

    @GetMapping
    public OffsetPagingResponse<JobPostSummaryDto> getJobPostSummaryList(@RequestParam("page") int page, @RequestParam(value = "title", required = false) String title) {
        if(title == null) title = "";
        return jobPostService.getJobPostSummaryList(page,20, title, null);
    }

    @GetMapping("/{jid}")
    public JobPostDetailResponse getJobPostDetail(@PathVariable("jid") Long jid) {
        return jobPostService.getJobPostDetail(jid);
    }

    @PostMapping
    public Long createJobPost(@RequestBody CreateJobPostRequest dto, @TokenUserEmail String userEmail) {
        return jobPostService.createJobPost(userEmail, dto);
    }

    @PutMapping("/{jid}")
    public void updateJobPost(@PathVariable("jid") Long jid, @RequestBody EditJobPostRequest dto, @TokenUserEmail String userEmail) {
        jobPostService.updateJobPost(userEmail, jid, dto);
    }

    @DeleteMapping("/{jid}")
    public void deleteJobPost(@PathVariable("jid") Long jid, @TokenUserEmail String userEmail) {
        jobPostService.deleteJobPost(userEmail, jid);
    }

    @PostMapping("/{jid}/comment")
    public Long createComment(@PathVariable("jid") Long jid, @RequestBody CreateCommentRequest dto, @TokenUserEmail String userEmail) {
        return jobPostService.createComment(userEmail, jid, dto);
    }
    @DeleteMapping("/{jid}/comment/{cid}")
    public void deleteComment(@PathVariable("jid") Long jid, @PathVariable("cid") Long cid, @TokenUserEmail String userEmail) {
        jobPostService.deleteComment(userEmail, jid, cid);
    }

}
