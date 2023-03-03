package com.example.demo;

import com.example.demo.filters.LoginFilter;
import com.example.demo.filters.ProfessorFilter;
import com.example.demo.repositories.StudentRepo;
import com.example.demo.services.AuthTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    @Autowired
    public FilterRegistrationBean<LoginFilter> loginFilter(AuthTokenService service) {
        FilterRegistrationBean<LoginFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new LoginFilter(service));
        registrationBean.addUrlPatterns("/api/grade/*", "/api/students/*", "/api/subject/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<ProfessorFilter> professorFilter() {
        FilterRegistrationBean<ProfessorFilter> professorFilterBean = new FilterRegistrationBean<>();
        professorFilterBean.setFilter(new ProfessorFilter());
        professorFilterBean.addUrlPatterns("/api/grade/professor", "/api/students/*", "/api/subject");
        professorFilterBean.setOrder(2);
        return professorFilterBean;
    }

}
