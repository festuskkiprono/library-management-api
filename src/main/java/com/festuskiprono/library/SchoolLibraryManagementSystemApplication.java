package com.festuskiprono.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("file:.env")
public class SchoolLibraryManagementSystemApplication {

	public static void main(String[] args) {


        SpringApplication.run(SchoolLibraryManagementSystemApplication.class, args);
        System.out.println("Hello World");
	}

}
