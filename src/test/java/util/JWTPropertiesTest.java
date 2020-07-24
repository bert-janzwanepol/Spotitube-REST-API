package util;

import oose.dea.spotitube.util.JWTProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JWTPropertiesTest {

    final String EXISTING_PROPERTY_KEY = "existing_property";
    final String NON_EXISTING_PROPERTY_KEY = "ytreporp_gnitsixe";

    @Test
    @DisplayName("Getting secret property returns property value if key exists")
    public void getReturnsExistingProperty() {
        JWTProperties jwtProperties = new JWTProperties();

        String loadedProperty = jwtProperties.getProperty(EXISTING_PROPERTY_KEY);

        assertEquals("i_exist", loadedProperty);
    }

    @Test
    @DisplayName("Getting secret property returns null if key does not exist")
    public void getReturnsNullIfPropertyNotExists() {
        JWTProperties jwtProperties = new JWTProperties();

        String loadedProperty = jwtProperties.getProperty(NON_EXISTING_PROPERTY_KEY);

        assertNull(loadedProperty);
    }

}
