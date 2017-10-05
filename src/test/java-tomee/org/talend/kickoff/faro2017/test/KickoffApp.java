package org.talend.kickoff.faro2017.test;

import lombok.Getter;

import org.apache.openejb.testing.Application;
import org.apache.openejb.testing.ContainerProperties;
import org.apache.openejb.testing.RandomPort;
import org.apache.tomee.embedded.Configuration;
import org.apache.tomee.embedded.TomEEEmbeddedApplicationRunner;

@Application
@ContainerProperties({@ContainerProperties.Property(name = "org.apache.batchee.init.verbose", value = "false")})
@TomEEEmbeddedApplicationRunner.Configurers(KickoffApp.MockRemoteServiceConfigurer.class)
public class KickoffApp {
    @Getter
    @RandomPort("http")
    private int port;

    public static class MockRemoteServiceConfigurer implements TomEEEmbeddedApplicationRunner.Configurer {
        @Override
        public void configure(final Configuration configuration) {
            final int port = configuration.getHttpPort();
            System.setProperty("cboe.symbols.url", "http://localhost:" + port + "/publish/ScheduledTask/MktData/cboesymboldir2.csv");
            System.setProperty("yahoo.price.url", "http://localhost:" + port + "/v10/finance/quoteSummary/{symbol}?modules=financialData");
        }
    }
}
