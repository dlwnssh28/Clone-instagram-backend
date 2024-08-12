package com.instagram.clone.auth;

import java.io.IOException;

import org.springframework.web.filter.GenericFilterBean;

import com.instagram.clone.jwt.JWTUtil;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomLogoutFilter extends GenericFilterBean {
    
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public CustomLogoutFilter(JWTUtil jwtUtil, RefreshRepository refreshRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        // 요청을 HttpServletRequest와 HttpServletResponse로 캐스팅
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 로그아웃 요청이 아닌 경우 필터 체인으로 요청을 넘김
        if (!"POST".equalsIgnoreCase(httpRequest.getMethod()) || !"/logout".equals(httpRequest.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        // 쿠키에서 refresh token 추출
        Cookie[] cookies = httpRequest.getCookies();
        String refresh = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh".equals(cookie.getName())) {
                    refresh = cookie.getValue();
                    break;
                }
            }
        }

        // refresh token이 없는 경우
        if (refresh == null) {
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 토큰이 만료되었는지 확인
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 토큰이 refresh 토큰인지 확인
        String category = jwtUtil.getCategory(refresh);
        if (!"refresh".equals(category)) {
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // DB에 저장된 refresh token인지 확인
        boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 로그아웃 처리 - refresh token DB에서 제거
        refreshRepository.deleteByRefresh(refresh);

        // 쿠키에서 refresh token 삭제
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        httpResponse.addCookie(cookie);

        // 로그아웃 성공 상태 코드 설정
        httpResponse.setStatus(HttpServletResponse.SC_OK);
    }
}
