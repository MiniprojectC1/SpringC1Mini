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

        // 로그인된 멤버 정보 가져오기
        Member member = userDetails.getMember();
        // 새로운 post 생성
        Post post = new Post(requestDto, member);
        // db에 post 저장
        postRepository.save(post);

        return ResponseDto.success( new PostResponseDto(post,null));
    }


    @Transactional(readOnly = true)
    public ResponseDto<?> getPost(Long id) {

        // id 로 게시글 존재 유무 확인
        Post post = isPresentPost(id);
        // 게시글에 작성된 댓글 모두 가져오기
        List<CommentResponseDto> comments = getAllCommentsByPost(post);

        return ResponseDto.success( new PostResponseDto(post, comments));

    }


    @Transactional(readOnly = true)
    public ResponseDto<?> getAllPost() {

        // db에 있는 모든 post 가져오기
        List<Post> postList = postRepository.findAllByOrderByModifiedAtDesc();
        // Comment 개수 check 후 postResponse 생성
        List<PostAllResponseDto> posts = getAllPosts(postList);

        return ResponseDto.success(posts);
    }



    @Transactional
    public ResponseDto<?> updatePost(Long id, PostRequestDto requestDto, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        // id 로 게시글 존재 유무 확인
        Post post = isPresentPost(id);
        // 게시글 작성자만이 수정 ,삭제 가능
        memberValidatePost(member, post);
        // 게시글에 작성된 댓글 모두 가져오기
        List<CommentResponseDto> comments = getAllCommentsByPost(post);
        // post 업데이트
        post.update(requestDto);
        return ResponseDto.success( new PostResponseDto(post,comments));
    }


    @Transactional
    public ResponseDto<?> deletePost(Long id, UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

        // id 로 게시글 존재 유무 확인
        Post post = isPresentPost(id);
        // 게시글 작성자만이 수정 ,삭제 가능
        memberValidatePost(member, post);
        // post 삭제
        postRepository.delete(post);
        return ResponseDto.success("delete success");

    }


        // id 로 게시글 존재 유무 확인
    @Transactional(readOnly = true)
    public Post isPresentPost(Long id) {

            Optional<Post> optionalPost = postRepository.findById(id);
            Post post = optionalPost.orElse(null);

        if (null == post) {
            throw new IllegalArgumentException("존재하지 않는 게시글 id 입니다");
        } else{
            return post;
        }
    }

        // 게시글 작성자만이 수정 ,삭제 가능
    @Transactional(readOnly = true)
    public void memberValidatePost(Member member, Post post) {
        if (post.validateMember(member)) {
            throw new IllegalArgumentException("게시글 작성자가 아닙니다");
        }
    }

     // 게시글에 작성된 댓글 모두 가져오기
    private List<CommentResponseDto> getAllCommentsByPost(Post post) {
        List<Comment> commentList = commentRepository.findAllByPost(post);
        List<CommentResponseDto> comments = new ArrayList<>();

        for (Comment comment : commentList) {
            comments.add(new CommentResponseDto(comment));
        }
        return comments;
    }

    // Comment 개수 check 후 모든 Post 가져오기
    private List<PostAllResponseDto> getAllPosts(List<Post> postList) {
        List<PostAllResponseDto> posts = new ArrayList<>();
        for(Post post : postList){
            List<Comment> commentList = commentRepository.findAllByPost(post);
            posts.add( new PostAllResponseDto(post, commentList));
        }
        return posts;
    }


    }

