package springc1.miniproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springc1.miniproject.controller.request.PostRequestDto;
import springc1.miniproject.controller.response.CommentResponseDto;
import springc1.miniproject.controller.response.PostAllResponseDto;
import springc1.miniproject.controller.response.PostResponseDto;
import springc1.miniproject.controller.response.ResponseDto;
import springc1.miniproject.domain.Comment;
import springc1.miniproject.domain.Member;
import springc1.miniproject.domain.Post;
import springc1.miniproject.domain.UserDetailsImpl;
import springc1.miniproject.repository.CommentRepository;
import springc1.miniproject.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PostService {


    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public ResponseDto<?> createPost(PostRequestDto requestDto, UserDetailsImpl userDetails ){

        Member member = userDetails.getMember();
        Post post = new Post(requestDto, member);
        postRepository.save(post);

        return ResponseDto.success( new PostResponseDto(post,null));
    }


    @Transactional(readOnly = true)
    public ResponseDto<?> getPost(Long id) {

        Post post = isPresentPost(id);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        List<Comment> commentList = commentRepository.findAllByPost(post);
        List<CommentResponseDto> comments = new ArrayList<>();

        for (Comment comment : commentList) {
            comments.add(new CommentResponseDto(comment));
        }

        return ResponseDto.success( new PostResponseDto(post, comments));

    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getAllPost() {

        List<Post> postList = postRepository.findAllByOrderByModifiedAtDesc();

        List<PostAllResponseDto> posts = new ArrayList<>();
        for(Post post : postList){
            List<Comment> commentList = commentRepository.findAllByPost(post);
            posts.add( new PostAllResponseDto(post, commentList));
        }

        return ResponseDto.success(posts);
    }


    @Transactional
    public ResponseDto<?> updatePost(Long id, PostRequestDto requestDto, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Post post = isPresentPost(id);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "post id is not exist");
        }

        if (post.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "only author can update");
        }

        List<Comment> commentList = commentRepository.findAllByPost(post);
        List<CommentResponseDto> comments = new ArrayList<>();

        for (Comment comment : commentList) {
            comments.add( new CommentResponseDto(comment));
        }

        post.update(requestDto);
        return ResponseDto.success( new PostResponseDto(post,comments));
    }


    @Transactional
    public ResponseDto<?> deletePost(Long id, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        Post post = isPresentPost(id);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "post id is not exist");
        }

        if (post.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "only author can delete");
        }

        postRepository.delete(post);
        return ResponseDto.success("delete success");

    }

        // id 로 게시글 존재 유무 확인
    @Transactional(readOnly = true)
    public Post isPresentPost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }


    }

