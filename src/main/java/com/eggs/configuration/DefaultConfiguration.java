package com.eggs.configuration;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.eggs.console.ConsoleMenuPrinter;
import com.eggs.domain.BaseMenuPrinter;
import com.eggs.domain.MenuRepositoryReader;
import com.eggs.impl.PackageScanSupport;

@Configuration
@Profile("default")
@ComponentScan(basePackageClasses={PackageScanSupport.class, ConsoleMenuPrinter.class})
public class DefaultConfiguration {

    @Autowired
    @Qualifier("memory")
    MenuRepositoryReader reader;

    @Autowired
    @Qualifier("console")
    BaseMenuPrinter printer;

    @PostConstruct
    public void wire() {
        printer.setReader(reader);
    }    
}
