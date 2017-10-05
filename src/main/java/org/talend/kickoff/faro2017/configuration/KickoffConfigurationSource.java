package org.talend.kickoff.faro2017.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.eclipse.microprofile.config.spi.ConfigSource;

// read our own properties file, can be an external one too or a "database"
public class KickoffConfigurationSource implements ConfigSource {

    private final Map<String, String> properties;

    public KickoffConfigurationSource() {
        final Properties loader = new Properties();
        try (final InputStream stream = Thread.currentThread().getContextClassLoader()
                              .getResourceAsStream("META-INF/kickoff.properties")) {
            if (stream != null) {
                loader.load(stream);
            }
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }

        properties = new HashMap<String, String>(Map.class.cast(loader));
    }

    @Override
    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public String getValue(final String key) {
        return properties.get(key);
    }

    @Override
    public String getName() {
        return "kickoff";
    }
}
