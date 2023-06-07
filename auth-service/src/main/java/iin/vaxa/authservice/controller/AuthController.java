package iin.vaxa.authservice.controller;

import iin.vaxa.authservice.dto.AuthResponse;
import iin.vaxa.authservice.dto.UserCred;
import iin.vaxa.authservice.service.AuthService;
import iin.vaxa.authservice.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  JwtTokenUtil jwtUtil;
  @Autowired
  private AuthService authService;
  @Autowired
  private AuthenticationManager authManager;

    @PostMapping("/register")
  public String createUser(@RequestBody UserCred user) {

    return authService.createUser(user);
  }

  @PostMapping("/authenticate")
  public ResponseEntity<?> createAuthenticationToken(@RequestBody UserCred user) throws Exception {

    try {
      authManager.authenticate(
          new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
    } catch (BadCredentialsException e) {
      throw new Exception("INVALID_CREDENTIALS", e);
    }

    final UserDetails userDetails = authService.loadUserByUsername(user.getUserName());

    final String token = jwtUtil.generateToken(userDetails);

    return ResponseEntity.ok(new AuthResponse(token));
  }

  @GetMapping("/validateToken")
  public ResponseEntity<String> validateToken(@RequestParam String token) {
    UserDetails userDetails = authService.loadUserByUsername(jwtUtil.getUsernameFromToken(token));
    jwtUtil.validateToken(token, userDetails);
    return new ResponseEntity<>("Token is valid", HttpStatus.OK);
  }

}
