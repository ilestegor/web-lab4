package ilestegor.lab4.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan("ilestegor.lab4")
@PropertySource("classpath:application.properties")
@EnableWebMvc
public class AppConfig implements WebMvcConfigurer {
}

