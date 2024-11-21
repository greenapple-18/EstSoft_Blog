package com.estsoft.springproject.controller;

import com.estsoft.springproject.entity.Member;
import com.estsoft.springproject.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    WebApplicationContext context;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MemberRepository repository;

    @BeforeEach
    public void setUp(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testGetAllMembers() throws Exception{
        Member savedMember = repository.save(new Member(1L, "홍길동"));

        ResultActions resultActions = mockMvc.perform(get("/members")
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$[0].id").value(savedMember.getId()))
                .andExpect(jsonPath("$[0].name").value(savedMember.getName()));
    }
}