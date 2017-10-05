package org.talend.kickoff.faro2017.configuration;

import java.util.Map;

import org.apache.deltaspike.core.spi.config.ConfigSource;

// ensure internal deltaspike config can reuse the app config (useful for scheduling/jpa modules)
public class DeltaspikeMicroConfigSource implements ConfigSource {
    private final KickoffConfigurationSource delegate = new KickoffConfigurationSource();

    @Override
    public Map<String, String> getProperties() {
        return delegate.getProperties();
    }

    @Override
    public String getPropertyValue(final String key) {
        return delegate.getValue(key);
    }

    @Override
    public String getConfigName() {
        return delegate.getName();
    }

    @Override
    public int getOrdinal() {
        return delegate.getOrdinal();
    }

    @Override
    public boolean isScannable() {
        return true;
    }
}
