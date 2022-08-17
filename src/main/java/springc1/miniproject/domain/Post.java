package springc1.miniproject.domain;

import lombok.*;
import springc1.miniproject.controller.request.PostRequestDto;

import javax.persistence.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String category;

    @Column(nullable = true)
    private String imgUrl;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public void update(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
    }

/*    public Post(PostRequestDto requestDto, Member member, String imgUrl) {

        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.category = requestDto.getCategory();
        this.member = member;
        this.imgUrl = imgUrl;
    }*/

    public Post(PostRequestDto requestDto, Member member) {

        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.category = requestDto.getCategory();
        this.member = member;
        this.imgUrl = requestDto.getImgUrl();
    }


    public boolean validateMember(Member member) {
        return !this.member.equals(member);
    }
}