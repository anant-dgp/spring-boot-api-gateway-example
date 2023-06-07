package iin.vaxa.authservice.service;

import iin.vaxa.authservice.dto.UserCred;
import iin.vaxa.authservice.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService implements UserDetailsService {
  List<UserCred> users = new ArrayList<>();
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  JwtTokenUtil jwtTokenUtil;

  public String createUser(UserCred user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    users.add(user);
    return "user created successfully";
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserCred user = users.stream().filter(u -> u.getUserName().equals(username)).findFirst().orElse(null);
    if (user != null) {
      return new User(user.getUserName(), user.getPassword(), new ArrayList<>());
    } else {
      throw new UsernameNotFoundException("User not found with username: " + username);
    }
  }
}
