package com.estsoft.springproject.user.config;

import com.estsoft.springproject.user.service.UserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfiguration {

    private final UserDetailService userDetailService;

    public WebSecurityConfiguration(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Bean
    public WebSecurityCustomizer ignore() {
        return webSecurity -> webSecurity.ignoring()
                .requestMatchers("/static/**", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests(
                custom -> custom.requestMatchers("/login", "/signup", "/user").permitAll()
//                        .requestMatchers("/articles/**").hasAnyAuthority("ADMIN")
//                        .anyRequest().authenticated()
                        .anyRequest().permitAll()
                )    // 3) 인증, 인가 설정
//                .requestMatchers("/login", "/signup", "/user").permitAll()
////                .requestMatchers("/api/external").hasRole("admin")
//                .anyRequest().authenticated()
                .formLogin(custom -> custom.loginPage("/login")
                        .defaultSuccessUrl("/articles", true))        //4) 폼 기반 로그인 설정
//                .loginPage("/login")
//                .defaultSuccessUrl("/articles")
                .logout(custom -> custom.logoutSuccessUrl("/login")
                        .invalidateHttpSession(true))       // 5) 로그아웃 설정
//                .logoutSuccessUrl("/login")
//                .invalidateHttpSession(true)
                // 6) csrf 비활성화
                .csrf(custom -> custom.disable())
//                .csrf().disable()
                .build();
    }

    // 7) 인증관리자 관련 설정
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService) {
//        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
//                .userDetailsService(userDetailService)  // 8) 사용자 정보 서비스 설정
//                .passwordEncoder(bCryptPasswordEncoder)
//                .and()
//                .build();
//    }

    // 9) 패스워드 인코더로 사용할 빈 등록
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
