package org.aery.practice.pcp;

import org.aery.practice.pcp.constant.PropertiesKeys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

@SpringBootApplication
public class RunPracticePCP {

	public static void main(String[] args) throws Throwable {
		try (ConfigurableApplicationContext application = SpringApplication.run(RunPracticePCP.class, args)) {
			processing(application);
		}
	}

	public static void processing(ConfigurableApplicationContext application) throws InterruptedException {
		ConfigurableEnvironment environment = application.getEnvironment();
		int exeMs = environment.getProperty(PropertiesKeys.EXE_MS, int.class, 1000);

		Thread.sleep(exeMs);
	}

}
