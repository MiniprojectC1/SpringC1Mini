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

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;
    private final TokenProvider tokenProvider;

    @Transactional
    public ResponseDto<?> createComment(CommentRequestDto requestDto, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Post post = postService.isPresentPost(requestDto.getPostId());
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }


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

        Post post = postService.isPresentPost(requestDto.getPostId());
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "post id is not exist");
        }

        Comment comment = isPresentComment(id);
        if (null == comment) {
            return ResponseDto.fail("NOT_FOUND", "comment id is not exist");
        }

        if (comment.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "only author can update");
        }


        comment.update(requestDto);
        return ResponseDto.success(new CommentResponseDto(comment));
    }


    @Transactional
    public ResponseDto<?> deleteComment(Long id, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Comment comment = isPresentComment(id);
        if (null == comment) {
            return ResponseDto.fail("NOT_FOUND", "comment id is not exist");
        }

        if (comment.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "only author can update");
        }

        commentRepository.delete(comment);
        return ResponseDto.success("success");

    }


    @Transactional(readOnly = true)
    public Comment isPresentComment(Long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        return optionalComment.orElse(null);
    }

    }
