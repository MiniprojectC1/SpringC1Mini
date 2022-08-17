package springc1.miniproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springc1.miniproject.controller.request.LoginRequestDto;
import springc1.miniproject.controller.request.MemberRequestDto;
import springc1.miniproject.controller.response.ResponseDto;
import springc1.miniproject.service.MemberService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    //api 회원가입
    @PostMapping(value = "/user/signup")
    public ResponseDto<?> signup(@RequestBody @Valid MemberRequestDto requestDto) {
        return memberService.createMember(requestDto);
    }

    //api 로그인
    @PostMapping(value = "/user/login")
    public ResponseDto<?> login(@RequestBody @Valid LoginRequestDto requestDto,
                                HttpServletResponse response
    ) {
        return memberService.login(requestDto, response);
    }
}