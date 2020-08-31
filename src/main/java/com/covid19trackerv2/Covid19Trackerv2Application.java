package com.covid19trackerv2;

import ch.qos.logback.core.spi.LifeCycle;
import com.covid19trackerv2.repository.UsStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class Covid19Trackerv2Application {

	public static void main(String[] args) {
		SpringApplication.run(Covid19Trackerv2Application.class, args);
	}

}
