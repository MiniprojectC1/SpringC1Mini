package springc1.miniproject.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springc1.miniproject.domain.Member;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
    private Long id;
    private String username;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public MemberResponseDto(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.nickname = member.getNickname();
        this.createdAt = member.getCreatedAt();
        this.modifiedAt = member.getModifiedAt();
    }

}
