package com.sparta.msa_exam.auth.service;

import com.sparta.msa_exam.auth.dto.SigninRequestDto;
import com.sparta.msa_exam.auth.dto.SignupRequestDto;
import com.sparta.msa_exam.auth.entity.User;
import com.sparta.msa_exam.auth.jwt.JwtUtil;
import com.sparta.msa_exam.auth.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.NotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  public void signup(SignupRequestDto signupRequestDto) {
    String username = signupRequestDto.getUsername();
    String password = passwordEncoder.encode(signupRequestDto.getPassword());

    Optional<User> checkUsername = userRepository.findByUsername(username);
    if (checkUsername.isPresent()) {
      throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
    }

    User user = new User(username, password);
    userRepository.save(user);
  }

  public String signin(SigninRequestDto requestDto, HttpServletResponse response) {

    String username = requestDto.getUsername();
    String password = requestDto.getPassword();

    User user = userRepository.findByUsername(username).orElseThrow(
        () -> new NotFoundException("Invalid Username")
    );

    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new IllegalArgumentException("Invalid Password");
    }

    String token = jwtUtil.createToken(username);
    jwtUtil.addJwtToCookie(token, response);

    return token;
  }
}
