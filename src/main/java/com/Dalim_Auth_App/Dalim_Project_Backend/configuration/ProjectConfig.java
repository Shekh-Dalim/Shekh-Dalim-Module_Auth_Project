package com.Dalim_Auth_App.Dalim_Project_Backend.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {

    @Bean  // TODO Tells Spring to create, manage, and provide this object wherever it is required.
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
