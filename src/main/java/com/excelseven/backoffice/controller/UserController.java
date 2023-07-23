package com.excelseven.backoffice.controller;

import com.excelseven.backoffice.dto.ApiResponseDto;
import com.excelseven.backoffice.dto.SigninRequestDto;
import com.excelseven.backoffice.dto.SignupRequestDto;
import com.excelseven.backoffice.jwt.JwtUtil;
import com.excelseven.backoffice.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final JwtUtil jwtUtil;

    // 회원가입 API
    @PostMapping("/signup")
    public ApiResponseDto signup(@RequestBody @Valid SignupRequestDto requestDto, BindingResult bindingResult) {
        log.info("bindingResult={}", bindingResult);
        if (bindingResult.hasErrors()) {
        // 에러 정보를 로그로 출력
            for (ObjectError error : bindingResult.getAllErrors()) {
                System.err.println(error.getDefaultMessage());
                return new ApiResponseDto(error.getDefaultMessage(),HttpStatus.BAD_REQUEST.value());
            }
        }

        try {
            userService.signup(requestDto);
        } catch (IllegalArgumentException e) {
            return new ApiResponseDto("중복된 사용자가 존재합니다.", HttpStatus.BAD_REQUEST.value());
        }
        return new ApiResponseDto("회원 가입에 성공하셨습니다.", HttpStatus.OK.value());
    }

    // 로그인 API
    @PostMapping("/signin")
    public ApiResponseDto signin(@RequestBody SigninRequestDto requestDto, HttpServletResponse response) {
        userService.signin(requestDto, response);
        return new ApiResponseDto("로그인 하셨습니다.", HttpStatus.OK.value());
    }


    // 로그아웃 API
    @PostMapping("/signout")
    public ApiResponseDto signout(HttpServletResponse response) {
        jwtUtil.expireCookie(response);
        return new ApiResponseDto("로그아웃 하셨습니다.", HttpStatus.OK.value());
    }
}
