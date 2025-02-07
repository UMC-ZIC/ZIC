package com.umc7.ZIC.security;

import com.umc7.ZIC.security.handler.CustomAccessDeniedHandler;
import com.umc7.ZIC.security.handler.CustomAuthenticationEntryPoint;
import com.umc7.ZIC.security.handler.ExceptionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler; // 핸들러 필드 추가
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final ExceptionFilter exceptionHandlerFilter; // ExceptionHandlerFilter 필드 추가
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomOAuth2UserService customOAuth2UserService) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers( "/api/kakao/home","/api/kakao/page","/api/kakao/login/oauth2","/api/kakao/page").permitAll()
                        .requestMatchers("/api/user/details", "/api/owner/details","/api/user/login").hasRole("PENDING")
                        .requestMatchers("/api/user/**").hasRole("USER")
                        .requestMatchers("/api/owner/**").hasRole("OWNER")
                        .requestMatchers("/api/reservation/**").hasAnyRole("USER", "OWNER")
                        .anyRequest().permitAll() //설정한 나머지는 아무나 가능.
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .redirectionEndpoint(endpoint -> endpoint.baseUri("/api/kakao/login/oauth2"))
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                                .userService(customOAuth2UserService))
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        .permitAll()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(exceptionHandlerFilter, JwtFilter.class);


        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",
                "https://localhost:3000",
                "http://localhost:5173",
                "https://localhost:5173",
                "http://localhost:8080",
                "https://localhost:8080",
                "http://43.200.3.214:8080",
                "https://43.200.3.214:8080",
                "http://zic-eight.vercel.app",
                "https://zic-eight.vercel.app"
        ));
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true); // 자격 증명 허용 (쿠키, 인증 정보 포함)
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
