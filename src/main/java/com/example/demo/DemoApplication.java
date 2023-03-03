package com.example.demo;

import com.example.demo.filters.LoginFilter;
import com.example.demo.repositories.StudentRepo;
import com.example.demo.services.AuthTokenService;
import com.google.common.hash.Hashing;
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
        ApplicationContext apc = SpringApplication.run(DemoApplication.class, args);
//       for(String s : apc.getBeanDefinitionNames()){
//           System.out.println(s);
//       }
    }

    @Bean
    CommandLineRunner runner(StudentRepo studentRepo) {
        return args -> {
//            Student student = new Student("Alex", "George", LocalDateTime.now());
//            studentRepo.insert(student);

        };
    }

//    @Bean
//    @Autowired
//    public FilterRegistrationBean<LoginFilter> loginFilter(AuthTokenService service) {
//        FilterRegistrationBean<LoginFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new LoginFilter(service));
//        registrationBean.addUrlPatterns("/api/students/add");
//        registrationBean.setOrder(1);
//        return registrationBean;
//    }

}
