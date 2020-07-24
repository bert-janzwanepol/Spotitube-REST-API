package oose.dea.spotitube.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class JWTProperties {

    private String propertiesFileName = "verysecret.properties";

    /**
     * Get's a property from the .properties file located in the Resources directory.
     * @param propertyname The key of the property in the .properties file
     * @return The value of the property, or null if it isn't found.
     */

    public String getProperty(String propertyname) {
        Properties properties = new Properties();

        try {
            String propertiesPath = getClass().getClassLoader().getResource(propertiesFileName).getPath();
            properties.load(new FileInputStream(propertiesPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties.getProperty(propertyname);
    }
}
