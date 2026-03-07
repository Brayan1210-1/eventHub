package com.cesde.eventhub;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;


public class TestEventhubApplication {
	



		public static void main(String[] args) {
			SpringApplication.from(EventhubApplication::main).with(TestcontainersConfiguration.class).run(args);
		}

	}


