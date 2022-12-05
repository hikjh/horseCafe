package stable.horseCafe.config.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import stable.horseCafe.config.jwt.JwtAuthenticationFilter;
import stable.horseCafe.config.jwt.JwtProvider;
import stable.horseCafe.web.common.exception.CustomAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf().disable()
                .headers().frameOptions().disable()
            .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
            .and()
                .authorizeRequests()
                .antMatchers("/h2-console/**", "/stable/v1/signUp", "/stable/v1/login").permitAll()
                .antMatchers("/stable/v1/menu/**").hasRole("MEMBER")
                .anyRequest().authenticated()
            .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider),UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
