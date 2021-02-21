package cn.locotor.springsecurityjwtexample.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.locotor.springsecurityjwtexample.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger jwtLogger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtTokenProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            UsernamePasswordAuthenticationToken authentication = verifyToken(jwt);
            if (authentication != null) {
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            jwtLogger.error("Could not set user authentication in security context", e);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken == null || !bearerToken.startsWith(JwtTokenProvider.TOKEN_PREFIX)) {
            jwtLogger.info("请求头不含 JWT token，调用下个过滤器");
            return null;
        }

        return bearerToken.split(" ")[1].trim();
    }

    // 验证token，并生成认证后的token
    private UsernamePasswordAuthenticationToken verifyToken(String token) {
        if (token == null) {
            return null;
        }

        // 认证失败，返回null
        if (!jwtProvider.validateToken(token)) {
            return null;
        }

        // 提取用户名
        String username = jwtProvider.getUsernameFromJWT(token);
        UserDetails userDetails = new User(username);

        // 构建认证过的token
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}