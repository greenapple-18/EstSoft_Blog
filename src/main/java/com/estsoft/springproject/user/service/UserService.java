package com.estsoft.springproject.user.service;

import com.estsoft.springproject.user.domain.Users;
import com.estsoft.springproject.user.domain.dto.AddUserRequest;
import com.estsoft.springproject.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public Users save(AddUserRequest dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();
        String encodedPassword = encoder.encode(password);

        Users users = new Users(email, encodedPassword);
        return userRepository.save(users);
    }

    public Users verifyExample(AddUserRequest dto){
        String email = dto.getEmail();
        String password = dto.getPassword();
        String encodedPassword = encoder.encode(password);

        return userRepository.save(new Users(email, encodedPassword));
    }
}
