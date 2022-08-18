package springc1.miniproject.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springc1.miniproject.domain.Post;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {

    private Long id;
    private String username;
    private String nickname;
    private String title;
    private String content;
    private String category;
    private String imgUrl;
    private List<CommentResponseDto> comments;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public PostResponseDto(Post post,List<CommentResponseDto> comments){
        this.id = post.getId();
        this.username = post.getMember().getUsername();
        this.nickname = post.getMember().getNickname();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory();
        this.imgUrl = post.getImgUrl();
        this.comments = comments;
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
    }

}
