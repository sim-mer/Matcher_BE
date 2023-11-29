package com.knu.matcher.service;

import com.knu.matcher.domain.jobpost.Comment;
import com.knu.matcher.domain.jobpost.CommentWithUser;
import com.knu.matcher.domain.jobpost.JobPost;
import com.knu.matcher.domain.jobpost.JobPostSummaryWithUser;
import com.knu.matcher.domain.user.User;
import com.knu.matcher.dto.common.OffsetPagingResponse;
import com.knu.matcher.dto.jobpost.*;
import com.knu.matcher.dto.user.UserInfoDto;
import com.knu.matcher.repository.CommentRepository;
import com.knu.matcher.repository.JobPostRepository;
import com.knu.matcher.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobPostService {
    private final JobPostRepository jobPostRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public OffsetPagingResponse<JobPostSummaryDto> getJobPostSummaryList(int page, int size, String title, String email) {
        List<JobPostSummaryWithUser> jobPostSummaryWithUserList;
        if(email == null) jobPostSummaryWithUserList = jobPostRepository.findJobPostSummaryList(page, size, title);
        else jobPostSummaryWithUserList = jobPostRepository.findJobPostSummaryList(page, size, title, email);

        List<JobPostSummaryDto> jobPostSummaryDtoList = jobPostSummaryWithUserList.stream()
                .map(JobPostSummaryDto::fromDomain).collect(Collectors.toList());
        Long total = jobPostRepository.getCountByTitle(title);
        boolean hasNext = !(total <= (long) (page + 1) * size);

        return new OffsetPagingResponse<>(hasNext, jobPostSummaryDtoList);
    }

    public JobPostDetailResponse getJobPostDetail(Long jobPostId) {
        JobPost jobPost = jobPostRepository.findById(jobPostId);
        if(jobPost == null){
            throw new IllegalArgumentException("게시글이 존재하지 않습니다.");
        }
        User author = userRepository.findByEmail(jobPost.getUserEmail());

        List<CommentWithUser> commentWithUserList = commentRepository.findCommentByJobPostIdWithUser(jobPostId);
        List<CommentDto> commentDtoList = commentWithUserList.stream().map(CommentDto::fromDomain).collect(Collectors.toList());


        return JobPostDetailResponse.builder()
                .id(jobPost.getId())
                .title(jobPost.getTitle())
                .date(jobPost.getDate())
                .author(UserInfoDto.fromDomain(author))
                .content(jobPost.getContent())
                .commentList(commentDtoList)
                .build();
    }

    public Long createJobPost(String userEmail, CreateJobPostRequest dto) {
        //Long id = jobPostRepository.findLastJobPostId() + 1;
        JobPost jobPost = JobPost.builder()
                .title(dto.getTitle())
                .date(LocalDateTime.now())
                .content(dto.getContent())
                .userEmail(userEmail)
                .build();
        Long jid = jobPostRepository.save(jobPost);
        if(jid == null){
            throw new IllegalStateException("게시글 작성에 실패하였습니다.");
        }
        return jid;
    }

    public void updateJobPost(String userEmail, Long id, EditJobPostRequest dto) {
        JobPost jobPost = jobPostRepository.findById(id);
        if(jobPost == null){
            throw new IllegalArgumentException("게시글이 존재하지 않습니다.");
        }
        if(!jobPost.getUserEmail().equals(userEmail)){
            throw new IllegalArgumentException("해당 게시글을 수정할 권한이 없습니다.");
        }
        JobPost editJobPost = JobPost.builder()
                .id(id)
                .title(dto.getTitle())
                .content(dto.getContent())
                .date(LocalDateTime.now())
                .userEmail(userEmail)
                .build();

        jobPostRepository.updateJobPost(editJobPost);
    }

    public void deleteJobPost(String userEmail, Long id) {
        JobPost jobPost = jobPostRepository.findById(id);
        if(jobPost == null){
            throw new IllegalArgumentException("게시글이 존재하지 않습니다.");
        }
        if(!jobPost.getUserEmail().equals(userEmail)){
            throw new IllegalArgumentException("해당 게시글을 삭제할 권한이 없습니다.");
        }
        jobPostRepository.deletePost(id);
    }

    public Long createComment(String userEmail, Long jobPostId, CreateCommentRequest dto) {
        Long id = commentRepository.findLastCommentId() + 1;
        Comment comment = Comment.builder()
                .id(id)
                .content(dto.getContent())
                .date(LocalDateTime.now())
                .userEmail(userEmail)
                .jobPostId(jobPostId)
                .build();
        Long cid = commentRepository.save(comment);
        if(cid == null){
            throw new IllegalStateException("댓글 작성에 실패하였습니다.");
        }
        return cid;
    }


    public void deleteComment(String userEmail, Long jobPostId, Long commentId){
        Comment comment = commentRepository.findById(commentId);
        if(comment == null){
            throw new IllegalArgumentException("댓글이 존재하지 않습니다.");
        }
        if(!comment.getUserEmail().equals(userEmail)){
            throw new IllegalArgumentException("해당 댓글을 삭제할 권한이 없습니다.");
        }
        commentRepository.deleteComment(commentId);
    }


}
