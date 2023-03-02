package com.example.demo.filters;

import com.example.demo.MultiReadHttpServletRequest;
import com.example.demo.services.AuthTokenService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

import java.io.IOException;

@AllArgsConstructor
public class LoginFilter implements Filter {
    private AuthTokenService authTokenService;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //ObjectMapper mapper = new ObjectMapper();
        MultiReadHttpServletRequest copiedRequest = new MultiReadHttpServletRequest((HttpServletRequest) servletRequest);
//        Map jsonMap = mapper.readValue(copiedRequest.getInputStream(),Map.class);
//        logger.info(jsonMap.toString());
//        logger.info("VALID!!");
//        logger.info(((HttpServletRequest) servletRequest).getHeader("username"));
        String userId = ((HttpServletRequest) servletRequest).getHeader("userId");
        String authorisationToken = ((HttpServletRequest) servletRequest).getHeader("authorisationToken");
        String refreshToken = ((HttpServletRequest) servletRequest).getHeader("refreshToken");
        if (!this.authTokenService.authenticateUser(userId, authorisationToken, refreshToken)) {
            ((HttpServletResponse) servletResponse).sendError(401, "Invalid token");
            return;
        }
        filterChain.doFilter(copiedRequest, servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
