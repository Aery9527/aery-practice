package org.aery.sorter;

import org.aery.sorter.api.Sorter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        try (ConfigurableApplicationContext application = SpringApplication.run(Application.class, args)) {
            Sorter sorter = application.getBean(Sorter.class);
            sorter.sort();
        }
    }

}
