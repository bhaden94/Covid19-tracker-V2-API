package com.covid19trackerv2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@SpringBootApplication
public class Covid19Trackerv2Application {

	public static void main(String[] args) {
		SpringApplication.run(Covid19Trackerv2Application.class, args);
	}
}
