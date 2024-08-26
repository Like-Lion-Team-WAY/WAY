package like.lion.way.config;


import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import like.lion.way.jwt.exception.CustomAuthenticationEntryPoint;
import like.lion.way.jwt.filter.JwtRequestFilter;
import like.lion.way.jwt.service.JwtUserDetailsService;
import like.lion.way.user.oauth2.handler.CustomOAuth2FailureHandler;
import like.lion.way.user.oauth2.handler.CustomOAuth2SuccessHandler;
import like.lion.way.user.oauth2.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    public static final List<String> PERMIT_ALL_PATHS = List.of(
            "/","/main" ,"/css/**", "/js/**","/user/login","/like","/duplicate","/addInterests","/oauth2/**","/oauth2/authorization/kakao","/login/oauth2/code/kakao","/admin/**","/image/**", "/interest/**",
            "https://kauth.kakao.com/oauth/authorize","https://kauth.kakao.com/oauth/token","https://kapi.kakao.com/v2/user/me","/posts/**","/posts/detail/**","/questions/send/**","/questions/reply/**","/questions/new/**"
            ,"/questions/create/**","/questions/create/**","/upload","/user/delete/**","/user/searchform","/user/search","/user/all"
        );



    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private CustomOAuth2SuccessHandler customOAuth2SuccessHandler;

    @Autowired
    private CustomOAuth2FailureHandler customOAuth2FailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        RequestMatcher permitAllMatcher = new RequestMatcher() {
            @Override
            public boolean matches(HttpServletRequest request) {
                return PERMIT_ALL_PATHS.stream().anyMatch(path-> new AntPathRequestMatcher(path).matches(request));
            }
        };

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeRequests(authorize -> authorize
                        .requestMatchers("/","/main" ,"/css/**", "/js/**","/user/login","/like","/duplicate","/addInterests","/oauth2/**","/oauth2/authorization/kakao","/login/oauth2/code/kakao","/admin/**","/image/**","/interest/**",
                                "https://kauth.kakao.com/oauth/authorize","https://kauth.kakao.com/oauth/token","https://kapi.kakao.com/v2/user/me","/posts/**","/posts/detail/**","/questions/send/**","/questions/reply/**","/questions/new/**"
                        ,"/questions/create/**","/questions/create/**","/upload","/user/delete/**","/user/searchform","/user/search","/user/all").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                )
                .httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .oauth2Login(oauth2-> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService))
                        .successHandler(customOAuth2SuccessHandler)
                        .failureHandler(customOAuth2FailureHandler)

                );

        return http.build();
    }

    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowedMethods(List.of("GET", "POST", "DELETE", "PATCH", "OPTIONS", "PUT"));
        config.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}