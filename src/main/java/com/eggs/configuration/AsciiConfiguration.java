package com.eggs.configuration;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.eggs.BaseMenuPrinter;
import com.eggs.MenuPrinter;
import com.eggs.MenuRepositoryReader;
import com.eggs.impl.AsciiArtPrinter;

@Configuration
@Profile("ascii")
public class AsciiConfiguration {

    @Autowired
    @Qualifier("yaml")
    MenuRepositoryReader reader;

    @Autowired
    @Qualifier("ascii")
    BaseMenuPrinter printer;

    @PostConstruct
    public void wire() {
        printer.setReader(reader);
    }
    
    @Bean
    public MenuPrinter mainPrinter() {
        return printer;
    }
    
}