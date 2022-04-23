package com.example.userservicemsa.security;

import com.example.userservicemsa.dto.RequestLogin;
import com.example.userservicemsa.dto.UserDTO;
import com.example.userservicemsa.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter{

    private UserService userService;
    private Environment environment;

    public AuthenticationFilter(AuthenticationManager authenticationManager, UserService userService, Environment environment) {
        super(authenticationManager);
        this.userService = userService;
        this.environment = environment;
    }

    // First call - 제일 처음 호출된다.
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        // log.info(request.getRemoteAddr());

        try {
            // POST로 데이터가 들어오기 때문에 파라메터로 못받는다, 그렇기에 InputStream을 사용해서 객체로 변환하자
            RequestLogin creds = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);

            // 로그인 객체를 token으로 만들기

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>()   // 권한 - Authorization List
                    )
            );
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }


    // Create Token and Insert Response
    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult)
            throws IOException, ServletException {
//        super.successfulAuthentication(request, response, chain, authResult); //REST API 이기에 필요없음
        String username = ((User)authResult.getPrincipal()).getUsername();

        // userDetail.pwd is current login pwd
        // userDetail.encryptedPwd is DB pwd
        UserDTO userDetails = userService.getUserDetailsByEmail(username);

        // signatureAlgorithm encrypt
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

        String token = Jwts.builder()
                .setSubject(userDetails.getUserId())
                .setExpiration(new Date(System.currentTimeMillis() +
                        Long.parseLong(environment.getProperty("token.expiration_time"))))
                .signWith(getSigninKey(environment.getProperty("token.secret")),SignatureAlgorithm.HS512)
                .compact();

        response.addHeader("token",token);
        response.addHeader("userId",userDetails.getUserId());
    }

    private Key getSigninKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
