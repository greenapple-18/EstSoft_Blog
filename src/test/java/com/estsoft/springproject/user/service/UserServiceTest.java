package com.estsoft.springproject.user.service;

import com.estsoft.springproject.user.domain.Users;
import com.estsoft.springproject.user.domain.dto.AddUserRequest;
import com.estsoft.springproject.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Spy
    BCryptPasswordEncoder encoder;

    @Test
    public void testSave() {
//        given:
        String email = "mock_email";
        String rawPassword = "mock_password";
        AddUserRequest request = new AddUserRequest();
        request.setEmail(email);
        request.setPassword(encoder.encode(rawPassword));

        when(userRepository.save(any())).thenReturn(new Users(request.getEmail(), request.getPassword()));

//        when:
        Users returnUser = userService.save(request);

//        then:
        assertThat(returnUser.getEmail(), is(email));
        assertEquals(request.getEmail(), returnUser.getEmail());
        assertEquals(request.getPassword(), returnUser.getPassword());

        verify(userRepository, times(1)).save(any(Users.class));
        verify(encoder, times(2)).encode(any(String.class));
    }

    @Test
    void testVerifyExample() {
        // given
        String email = "test@example.com";
        String password = "password123";
        String encodedPassword = "encodedPassword123";
        AddUserRequest request = new AddUserRequest(email, password);

        Users savedUser = new Users(email, encodedPassword);

        when(encoder.encode(password)).thenReturn(encodedPassword);
        when(userRepository.save(any(Users.class))).thenReturn(savedUser);

        // when
        Users result = userService.verifyExample(request);

        // then
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals(encodedPassword, result.getPassword());

        verify(encoder, times(1)).encode(password);
        verify(userRepository, times(1)).save(any(Users.class));
    }
}