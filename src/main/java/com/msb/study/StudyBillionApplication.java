package com.msb.study;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(value = "com.msb.study.mapper")
@SpringBootApplication
public class StudyBillionApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudyBillionApplication.class, args);
	}

}
