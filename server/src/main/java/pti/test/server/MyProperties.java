package pti.test.server;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * Class for property list creating. Singleton.
 *
 * @author Syrotyuk R.
 */
public class MyProperties extends Properties{

    @Autowired
    private static Logger logger;

    private static Properties properties;

    private MyProperties() {
    }

    /**
     * Getter fot PropertyList instance. Fills a property list from application properties file.
     *
     * @return the only one PropertyList instance
     */
    public static Properties getInstance() {
        if (properties == null) {
            properties = getProperties();
        }
        return properties;
    }

    private static Properties getProperties() {
        properties = new Properties();
        try (FileInputStream input = new FileInputStream("project.properties")) {
            properties.load(input);
            properties.entrySet().forEach(x -> System.out.println(x.getKey() + " " + x.getValue()));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return properties;
    }


}
