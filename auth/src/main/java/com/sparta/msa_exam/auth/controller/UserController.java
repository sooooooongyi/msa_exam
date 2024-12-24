package com.sparta.msa_exam.auth.controller;

import com.sparta.msa_exam.auth.dto.SigninRequestDto;
import com.sparta.msa_exam.auth.dto.SignupRequestDto;
import com.sparta.msa_exam.auth.response.AuthResponse;
import com.sparta.msa_exam.auth.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/auth/signup")
  public void signup(@RequestBody SignupRequestDto signupRequestDto) {
    userService.signup(signupRequestDto);
  }

  @PostMapping("/auth/signin")
  public ResponseEntity<?> signin(@RequestBody SigninRequestDto signinRequestDto,
      HttpServletResponse response) {
    String token = userService.signin(signinRequestDto, response);
    return ResponseEntity.ok(new AuthResponse(token));
  }
}
