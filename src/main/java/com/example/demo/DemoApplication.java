package com.example.demo;

import com.example.demo.filters.LoginFilter;
import com.example.demo.filters.ProfessorFilter;
import com.example.demo.repositories.StudentRepo;
import com.example.demo.services.AuthTokenService;
import com.example.demo.services.EmailSenderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;

import java.util.Random;


@SpringBootApplication
public class DemoApplication {
    private static final Logger logger = LogManager.getLogger();
    public static void main(String[] args) {
        logger.warn("Entering main");
        SpringApplication.run(DemoApplication.class, args);
    }
//    @EventListener(ApplicationReadyEvent.class)
//    void send(){
//        Random random = new Random();
//        System.out.println(random.nextInt());
//        emailSenderService.sendEmail("afenesan60@gmail.com","Salut ce faci?","Testut2");
//    }

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
        professorFilterBean.addUrlPatterns("/api/grade/professorGrades", "/api/students/*", "/api/subject");
        professorFilterBean.setOrder(2);
        return professorFilterBean;
    }

}
