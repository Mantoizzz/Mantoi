package com.forum.mantoi.sys.services;

import com.forum.mantoi.sys.model.JwtUser;
import com.forum.mantoi.sys.entity.User;
import com.forum.mantoi.sys.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return new JwtUser(user.get());
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
