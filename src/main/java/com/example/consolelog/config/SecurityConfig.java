package com.example.consolelog.config;

import com.example.consolelog.exception.JwtAccessDeniedHandler;
import com.example.consolelog.security.JwtAuthenticationEntryPoint;
import com.example.consolelog.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsUtils;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@ConditionalOnDefaultWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SecurityConfig{
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // h2 database 테스트가 원활하도록 관련 API 들은 전부 무시

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return  (web) -> web.ignoring()
                .antMatchers( "/favicon.ico");
    }

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CSRF 설정 Disable
        http.cors();

        http.csrf().disable()


                // exception handling 할 때 우리가 만든 클래스를 추가
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // h2-console 을 위한 설정을 추가
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                // 시큐리티는 기본적으로 세션을 사용
                // 여기서는 세션을 사용하지 않기 때문에 세션 설정을 Stateless 로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 로그인, 회원가입 API 는 토큰이 없는 상태에서 요청이 들어오기 때문에 permitAll 설정
                .and()
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers("/members/**").permitAll()
                .antMatchers(HttpMethod.POST).authenticated()
                .antMatchers(HttpMethod.DELETE).authenticated()
                .antMatchers(HttpMethod.PUT).authenticated()
                .antMatchers("/mypage/**").authenticated()
                .anyRequest()
                .permitAll()


                .and()
                .apply(new JwtSecurityConfig(tokenProvider));


        return http.build();
    }
}
//
//구글 로그인 과정
//
//1. 구글 로그인 버튼 클릭시 구글에 요청 전송
//2-1. 유저가 구글 미 로그인 상태일시 로그인 창 띄움
//2-2. 유저가 구글 계정 로그인
//3-1. 앱을 허용한 적이 없다면 Oauth 2.0 창을 띄움
//3-2. 유저는 앱이 요청하는 권한 확인하고 허가
//4. 구글에서 일회용 Authorization Code를 클라이언트에 전달
//5. 클라이언트에서 해당 코드를 백엔드로 전송
//6. 받은 코드를 이용해 구글 서버에 토큰 발급 요청
//7. 구글에서 해당 백엔드 서버로 토큰 발급
//8. 발급 받은 토큰으로 해당 유저 정보 조회 요청
//9. 구글 서버에서 요청 받은 유저 정보 반환
//10. 백엔드 서버에서 해당 유저의 email이 db에 있는지 조회하고 있으면
//    Jwt 토큰과 함께 유저 정보 반환.

//받아야할 데이터 - Email (필수!!)
