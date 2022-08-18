package springc1.miniproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springc1.miniproject.controller.request.LoginRequestDto;
import springc1.miniproject.controller.request.MemberRequestDto;
import springc1.miniproject.controller.request.TokenDto;
import springc1.miniproject.controller.response.MemberResponseDto;
import springc1.miniproject.controller.response.ResponseDto;
import springc1.miniproject.domain.Member;
import springc1.miniproject.domain.Post;
import springc1.miniproject.jwt.TokenProvider;
import springc1.miniproject.repository.MemberRepository;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional
    public ResponseDto<?> createMember(MemberRequestDto requestDto) {

        //중복된 닉네임 확인
        isPresentMember(requestDto.getUsername());
        //패스워드 인코딩
        String password = passwordEncoder.encode(requestDto.getPassword());
        // 멤버 생성
        Member member = new Member(requestDto, password);
        //db에 멤버 저장
        memberRepository.save(member);
        return ResponseDto.success( new MemberResponseDto(member));

    }


    @Transactional
    public ResponseDto<?> login(LoginRequestDto requestDto, HttpServletResponse response) {

        // 아이디 존재 확인 , 비밀번호 확인 , 로그인 검증
        Member member = memberCheck(requestDto);
        // 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        // response header에 토큰 저장
        tokenToHeaders(tokenDto, response);

        return ResponseDto.success(new MemberResponseDto(member));

    }



    // 중복된 닉네임 확인
    @Transactional(readOnly = true)
    public void isPresentMember(String username) {
        Optional<Member> optionalMember = memberRepository.findByUsername(username);
        Member member = optionalMember.orElse(null);

        if (null != member) {
            throw new IllegalArgumentException("중복된 닉네임 입니다.");
        }
    }


    // 아이디 존재 확인 , 비밀번호 확인 , 로그인 검증
    private Member memberCheck(LoginRequestDto requestDto) {
        Optional<Member> optionalMember = memberRepository.findByUsername(requestDto.getUsername());
         Member member = optionalMember.orElse(null);

         if (member == null){
             System.out.println("멤버 없음");
             throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
         } else if (!member.validatePassword(passwordEncoder, requestDto.getPassword())){
             System.out.println("비밀번호 다름");
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
         } else return member;

    }

    // response header에 토큰 저장
    public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
        response.addHeader("AccessToken", "Bearer " + tokenDto.getAccessToken());
    }



}
