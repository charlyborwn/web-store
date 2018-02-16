package pti.test.server.config;

import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
/**
 * @author Syrotyuk R.
 */
@EnableAutoConfiguration
@ComponentScan(value = {"pti.test"})
@Configuration
public class LoggingConfiguration {

    @Bean
    public Logger logger(){
        return Logger.getLogger("application");
    }

}