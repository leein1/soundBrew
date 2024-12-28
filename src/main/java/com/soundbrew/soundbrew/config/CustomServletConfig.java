package com.soundbrew.soundbrew.config;

import org.springframework.context.annotation.Configuration;
<<<<<<< HEAD
=======
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
>>>>>>> feature/kyoung
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
<<<<<<< HEAD
=======
@EnableWebMvc
>>>>>>> feature/kyoung
public class CustomServletConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
<<<<<<< HEAD
=======

>>>>>>> feature/kyoung
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/static/assets/");
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/");
    }
<<<<<<< HEAD


=======
>>>>>>> feature/kyoung
}
