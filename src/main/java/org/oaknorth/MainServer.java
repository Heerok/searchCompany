package org.oaknorth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

/**
 * Created by Heerok on 24-09-2016.
 */
@SpringBootApplication
public class MainServer{

    public static void main(String[] args) throws Exception{
        SpringApplication.run(MainServer.class,args);
    }

}
