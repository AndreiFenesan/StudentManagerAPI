package com.example.demo.filters;

import com.example.demo.domain.UserType;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ProfessorFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        UserType userType = (UserType) servletRequest.getAttribute("userType");
        if (!userType.equals(UserType.PROFESSOR)) {
            ((HttpServletResponse) servletResponse).sendError(401, "Permission denied");
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
