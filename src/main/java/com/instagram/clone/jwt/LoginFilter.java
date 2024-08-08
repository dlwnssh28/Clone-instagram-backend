package com.instagram.clone.jwt;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.instagram.clone.auth.LoginDTO;
import com.instagram.clone.user.CustomUserDetails;
import com.instagram.clone.user.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {
	
	private final AuthenticationManager authenticationManager;
	
	private final JWTUtil jwtUtil;
	
    private final CustomUserDetailsService userDetailsService;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, CustomUserDetailsService userDetailsService) {

        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }
	
    //로그인 필터
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		LoginDTO loginDTO = new LoginDTO();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            loginDTO = objectMapper.readValue(messageBody, LoginDTO.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String login = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        CustomUserDetails user = (CustomUserDetails) userDetailsService.loadUserByUsername(login);
        String username = user.getUsername();
		System.out.println(username);
		//스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);
		
		//token에 담은 검증을 위한 AuthenticationManager로 전달
		return authenticationManager.authenticate(authToken);
	}

	//로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
		
		CustomUserDetails customUserDetails = (CustomUserDetails)authentication.getPrincipal();
		
		String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String token = jwtUtil.createJwt(username, role, 60 * 60 * 1000L);
        
        response.addHeader("Authorization", "Bearer " + token);
		System.out.println("JWT Token: " + token);
	}
	
		//로그인 실패시 실행하는 메소드
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
		//로그인 실패시 401 응답 코드 반환
        response.setStatus(401);
	}
}
