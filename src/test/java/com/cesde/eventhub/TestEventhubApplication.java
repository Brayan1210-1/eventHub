package com.cesde.eventhub;


import org.springframework.boot.SpringApplication;


public class TestEventhubApplication {
	



		public static void main(String[] args) {
			SpringApplication.from(EventhubApplication::main).with(TestcontainersConfiguration.class).run(args);
		}

	}


