package com.knu.matcher.controller;

import com.knu.matcher.dto.jobpost.CreateCommentRequest;
import com.knu.matcher.dto.jobpost.CreateJobPostRequest;
import com.knu.matcher.dto.jobpost.EditJobPostRequest;
import com.knu.matcher.service.JobPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jobpost")
public class JobPostController {
    private final JobPostService jobPostService;

    @GetMapping
    public Long createJobPost(@RequestBody CreateJobPostRequest dto) {
        String userEmail = "user2@example.com";
        return jobPostService.createJobPost(userEmail, dto);
    }

    @PutMapping("/{jid}")
    public void updateJobPost(@PathVariable("jid") Long jid, @RequestBody EditJobPostRequest dto) {
        String userEmail = "user2@example.com";
        jobPostService.updateJobPost(userEmail, jid, dto);
    }

    @DeleteMapping("/{jid}")
    public void deleteJobPost(@PathVariable("jid") Long jid) {
        String userEmail = "user2@example.com";
        jobPostService.deleteJobPost(userEmail, jid);
    }

    @PostMapping("/{jid}/comment")
    public Long createComment(@PathVariable("jid") Long jid, @RequestBody CreateCommentRequest dto) {
        String userEmail = "user2@example.com";
        return jobPostService.createComment(userEmail, jid, dto);
    }
    @DeleteMapping("/{jid}/comment/{cid}")
    public void deleteComment(@PathVariable("jid") Long jid, @PathVariable("cid") Long cid) {
        String userEmail = "user2@example.com";
        jobPostService.deleteComment(userEmail, jid, cid);
    }

}
