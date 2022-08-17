package springc1.miniproject.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springc1.miniproject.domain.Comment;
import springc1.miniproject.domain.Post;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor

//memo PostController 에서 getAllPosts 호출할때 리턴할 responseDto

public class PostAllResponseDto {
    private Long id;
    private String nickname;
    private String title;
    private String content;
    private String category;
    private String imgUrl;
    private Long commentsNum;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public PostAllResponseDto(Post post, List<Comment> commentList) {
        this.id = post.getId();
        this.nickname = post.getMember().getNickname();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory();
        this.imgUrl = post.getImgUrl();
        this.commentsNum = (long) commentList.size();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
    }
}

