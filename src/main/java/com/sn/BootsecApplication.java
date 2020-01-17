package com.sn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.sn.Mapper")
@SpringBootApplication
public class BootsecApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootsecApplication.class, args);
	}

}
