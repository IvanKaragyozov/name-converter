package pu.nameconverter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public StringBuilder stringBuilder(){
        return new StringBuilder();
    }
}
