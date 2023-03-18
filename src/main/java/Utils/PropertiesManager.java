package Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesManager {

    private static PropertiesManager instance;
    private Properties properties;

    private PropertiesManager() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            properties = new Properties();
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized Properties getProperties() {
        if (instance == null) {
            instance = new PropertiesManager();
        }
        return instance.properties;
    }
}