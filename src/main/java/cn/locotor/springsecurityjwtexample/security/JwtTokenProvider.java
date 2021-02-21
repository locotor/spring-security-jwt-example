package cn.locotor.springsecurityjwtexample.security;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import cn.locotor.springsecurityjwtexample.model.User;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    private static final long JWT_EXPIRATION = 5 * 60 * 1000L;

    public static final String TOKEN_PREFIX = "Bearer ";

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    public String generateToken(Authentication authentication) {
        User userPrincipal = (User) authentication.getPrincipal();
        Date expireDate = new Date(System.currentTimeMillis() + JWT_EXPIRATION);
        try {
            // 创建签名的算法实例
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            return JWT.create().withExpiresAt(expireDate).withClaim("username", userPrincipal.getUsername())
                    .sign(algorithm);
        } catch (JWTCreationException jwtCreationException) {
            logger.warn("Token create failed");
            return null;
        }
    }

    public String getUsernameFromJWT(String authToken) {
        try {
            DecodedJWT jwt = JWT.decode(authToken);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException jwtDecodeException) {
            logger.warn("get username by decoding token failed");
            return null;
        }
    }

    public boolean validateToken(String authToken) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            // 构建 JWT 验证器，token合法同时 payload 必须含有私有字段 username 且值一致
            // token 过期也会验证失败
            JWTVerifier verifier = JWT.require(algorithm).build();
            // 验证token
            verifier.verify(authToken);
            return true;
        } catch (JWTVerificationException jwtVerificationException) {
            logger.warn("verify token field");
            return false;
        }
    }
}