package cn.locotor.springsecurityjwtexample.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import cn.locotor.springsecurityjwtexample.common.CodeMessage;
import cn.locotor.springsecurityjwtexample.common.ResponseData;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        ResponseData respBean = setResponseData(exception);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getWriter(), respBean);
    }

    private ResponseData setResponseData(AuthenticationException exception) {
        if (exception instanceof LockedException) {
            return ResponseData.build(CodeMessage.USER_IS_LOCK);
        } else if (exception instanceof CredentialsExpiredException) {
            return ResponseData.build(CodeMessage.CREDENTIAL_NOT_RIGHT);
        } else if (exception instanceof AccountExpiredException) {
            return ResponseData.build(CodeMessage.USER_NOT_RIGHT);
        } else if (exception instanceof DisabledException) {
            return ResponseData.build(CodeMessage.USER_DISABLE);
        } else if (exception instanceof BadCredentialsException) {
            return ResponseData.build(CodeMessage.AUTH_ERROR);
        }
        return ResponseData.build(CodeMessage.USER_NOT_LOGIN);
    }

}
