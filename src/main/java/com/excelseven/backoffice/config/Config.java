package com.excelseven.backoffice.config;

import com.excelseven.backoffice.jwt.JwtAuthorizationFilter;
import com.excelseven.backoffice.jwt.JwtUtil;
import com.excelseven.backoffice.security.UserDetailsServicelmpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class Config {

    private final JwtUtil jwtUtil;
    private final UserDetailsServicelmpl userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable()); //csrf.disable 사이트간요청위조 보호 적용 해제

        /**
         *  기본 설정을 사용하지 않고, securityFilterChain 을 재정의함
         *  그리고 모든 요청을 대해 허용으로 바꿈
         */
//        http.authorizeHttpRequests(requests -> requests.anyRequest().permitAll()) // 모든 경로 허용
//                .formLogin(Customizer.withDefaults())
//                .httpBasic(Customizer.withDefaults());

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
            return configuration.getAuthenticationManager();
        }

        @Bean
        public JwtAuthorizationFilter jwtAuthorizationFilter() {
            return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
        }

        return http.build();

            //특정 경로만 허용할 때 위에 내용 주석하고 아래내용 주석풀기
//        http.authorizeHttpRequests((requests) -> requests
//                        .requestMatchers("/myAccount","/myBalance","/myLoans","/myCards").authenticated() //특정 경로만 인증하고 싶을 때 authenticated: 인증이란 뜻
//                        .requestMatchers("/hello").permitAll()) // 특정 경로만 허용하고 싶을 때
//
//                .formLogin(Customizer.withDefaults()) // 로그인 폼 기본인증 방식 기본설정으로 한다.
//                .httpBasic(Customizer.withDefaults()); //httpBasic 기본인증 방식 기본설정으로 한다.
//        return http.build();
    }
}