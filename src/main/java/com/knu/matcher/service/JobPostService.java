package com.knu.matcher.service;

import com.knu.matcher.domain.jobpost.Comment;
import com.knu.matcher.domain.jobpost.JobPost;
import com.knu.matcher.dto.jobpost.CreateCommentRequest;
import com.knu.matcher.dto.jobpost.CreateJobPostRequest;
import com.knu.matcher.dto.jobpost.EditJobPostRequest;
import com.knu.matcher.repository.CommentRepository;
import com.knu.matcher.repository.JobPostRepository;
import com.knu.matcher.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class JobPostService {
    private final JobPostRepository jobPostRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public Long createJobPost(String userEmail, CreateJobPostRequest dto) {
        Long id = jobPostRepository.findLastJobPostId() + 1;
        JobPost jobPost = JobPost.builder()
                .id(id)
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
