package springc1.miniproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springc1.miniproject.controller.request.PostRequestDto;
import springc1.miniproject.controller.response.ResponseDto;
import springc1.miniproject.domain.UserDetailsImpl;
import springc1.miniproject.service.PostService;




@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;


    //api 게시글 작성
    @PostMapping("/api/post")
    public ResponseDto<?> createPost(@RequestBody PostRequestDto requestDto,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.createPost(requestDto, userDetails);
    }


    //api id로 게시글 조회
    @GetMapping( "/api/post/{id}")
    public ResponseDto<?> getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }


    //api 게시글 카테고리 조회
    @GetMapping( "/api/posts")
    public ResponseDto<?> getAllPosts() {
        return postService.getAllPost();
    }


    //api 게시글 수정
    @PutMapping("/api/post/{id}")
    public ResponseDto<?> updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.updatePost(id, postRequestDto,userDetails);
    }


    //api 게시글 삭제
    @DeleteMapping( "/api/post/{id}")
    public ResponseDto<?> deletePost(@PathVariable Long id,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.deletePost(id,userDetails);
    }
}
