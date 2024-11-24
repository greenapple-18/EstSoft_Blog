package com.estsoft.springproject.user.repository;

import com.estsoft.springproject.user.domain.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByEmail() {
        // given
        Users user = getUser();
        userRepository.save(user);

        // when
        Users returnUser = userRepository.findByEmail(user.getEmail()).orElseThrow();

        // then
        assertThat(returnUser.getEmail(),is(user.getEmail()));
        assertThat(returnUser.getPassword(), is(user.getPassword()));
    }

    private Users getUser() {
        String email = "test@test.com";
        String password = "pw1234567";
        return new Users(email, password);
    }

    @Test
    public void testSave() {
        // given
        Users user = getUser();

        // when
        Users saved = userRepository.save(user);

        // then
        assertThat(saved.getEmail(), is(user.getEmail()));
    }

    @Test
    public void testFindAll() {
        //given
        userRepository.save(getUser());
        userRepository.save(getUser());
        userRepository.save(getUser());

        //when
        List<Users> usersList = userRepository.findAll();

        //then
        assertThat(usersList.size(), is(3));
    }
}