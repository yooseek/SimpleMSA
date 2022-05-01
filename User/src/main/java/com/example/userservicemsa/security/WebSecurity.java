package com.example.userservicemsa.security;

import com.example.userservicemsa.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private Environment environment;

    public WebSecurity(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder, Environment environment) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.environment = environment;
    }

    // Config for Authorization
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();
//        http.authorizeRequests().antMatchers("/users/**").permitAll();
        http.authorizeRequests().antMatchers("/**")
//                .access("hasIpAddress('"+ "121.167.204.55" + "')")
//                .hasIpAddress("121.167.204.15") // 허용할 IP 주소 - 스프링 버전 문제.. 낮은 버전은 이 주석대로..
                .access("hasIpAddress('"+ environment.getProperty("gateway.ip") + "')")
                .and()
                .addFilter(getAuthenticationFilter()); // 인증 과정에 필터 등록

        http.headers().frameOptions().disable(); // Enable h2-console
    }

    // Config for Authentication
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);

    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        // filter 선언
        AuthenticationFilter authenticationFilter =
                new AuthenticationFilter(authenticationManager(),userService,environment);
        // Add authenticationManager on filter
        // authenticationFilter.setAuthenticationManager(authenticationManager());

        return authenticationFilter;
    }
}
