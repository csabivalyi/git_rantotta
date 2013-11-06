package com.eggs.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;

import com.eggs.domain.Menu;
import com.eggs.domain.MenuBuilder;
import com.eggs.domain.MenuRepository;
import com.eggs.domain.MenuRepositoryReader;

public class CsvFileMenuRepositoryReader implements MenuRepositoryReader {

    private Logger logger = LoggerFactory.getLogger(CsvFileMenuRepositoryReader.class);
    private String[] restaurantNames;
    
    @Autowired
    private ApplicationContext ctx;

    public CsvFileMenuRepositoryReader(String... restaurantNames) {
        this.restaurantNames = restaurantNames;
    }

    private Menu processSingleRestaurant(String restaurant) {
        logger.debug("processing next restaurant: {}", restaurant);
        String filename = restaurant + ".csv";
        logger.debug("restaurant is read from file: {}", filename);

        MenuBuilder builder = ctx.getBean(MenuBuilder.class);
        builder.restaurant(restaurant);

        Resource file = ctx.getResource(filename);
        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String line = "";
            try {
                while ((line = reader.readLine()) != null) {
                    processMenuLine(builder, line);
                }
            } catch (IOException e) {
                logger.error("couldnt process LINE: " + line, e);
            }

        } catch (IOException e1) {
            logger.error("Resource Error: ", e1);
        }        

        return builder.build();
    }

    private void processMenuLine(MenuBuilder builder, String line) {
        logger.trace("processing line: {}", line);
        String[] fields = line.split(",");
        float price = Float.parseFloat(fields[2]);
        builder.food(fields[0].trim(), fields[1].trim(), price);
    }

    public MenuRepository read() {
        List<Menu> menus = new ArrayList<Menu>();

        for (String restaurant : restaurantNames) {
            Menu menu = processSingleRestaurant(restaurant);
            menus.add(menu);
        }

        MenuRepository repo = new MenuRepository(menus);
        return repo;
    }

    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        this.ctx = ctx;
        
    }
}
