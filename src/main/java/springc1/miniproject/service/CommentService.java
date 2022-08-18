package springc1.miniproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springc1.miniproject.controller.request.CommentRequestDto;
import springc1.miniproject.controller.response.CommentResponseDto;
import springc1.miniproject.controller.response.ResponseDto;
import springc1.miniproject.domain.Comment;
import springc1.miniproject.domain.Member;
import springc1.miniproject.domain.Post;
import springc1.miniproject.domain.UserDetailsImpl;
import springc1.miniproject.jwt.TokenProvider;
import springc1.miniproject.repository.CommentRepository;
import springc1.miniproject.repository.PostRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    private final PostService postService;

    @Transactional
    public ResponseDto<?> createComment(CommentRequestDto requestDto, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();
        Post post = postService.isPresentPost(requestDto.getPostId());

        Comment comment = Comment.builder()
                .member(member)
                .post(post)
                .content(requestDto.getContent())
                .build();
        commentRepository.save(comment);
        return ResponseDto.success( new CommentResponseDto(comment));
    }


    @Transactional
    public ResponseDto<?> updateComment(Long id, CommentRequestDto requestDto, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();
        // id 로 댓글 존재 유무 확인
        postService.isPresentPost(requestDto.getPostId());
        // id 로 댓글 존재 유무 확인
        Comment comment = isPresentComment(id);
        // 댓글 작성자만이 수정 가능
        memberValidateComment(member, comment);
        // 댓글 업데이트
        comment.update(requestDto);
        return ResponseDto.success(new CommentResponseDto(comment));
    }


    @Transactional
    public ResponseDto<?> deleteComment(Long id, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        // id 로 댓글 존재 유무 확인
        Comment comment = isPresentComment(id);
        // 댓글 작성자만이 수정 삭제 가능
        memberValidateComment(member, comment);
        // 댓글 삭제
        commentRepository.delete(comment);
        return ResponseDto.success("success");

    }


    // id 로 댓글 존재 유무 확인
    @Transactional(readOnly = true)
    public Comment isPresentComment(Long id) {

        Optional<Comment> optionalComment = commentRepository.findById(id);
        Comment comment = optionalComment.orElse(null);

        if (null == comment) {
            throw new IllegalArgumentException("존재하지 않는 댓글 id 입니다");
        } else{
            return comment;
        }
    }

    // 댓글 작성자만이 수정 ,삭제 가능
    @Transactional(readOnly = true)
    public void memberValidateComment(Member member, Comment comment) {
        if (comment.validateMember(member)) {
            throw new IllegalArgumentException("댓글 작성자가 아닙니다");
        }
    }


    }
