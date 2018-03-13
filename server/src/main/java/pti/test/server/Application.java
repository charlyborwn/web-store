package pti.test.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

/**
 * Spring boot class to run the application.
 *
 * @author Syrotyuk R.
 */
@SpringBootApplication
@ComponentScan(value = {"pti.test"})
@EnableCaching
public class Application {

    /**
     * Entering point of the application. Method runs a SpringApplication.
     *
     * @param args input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
