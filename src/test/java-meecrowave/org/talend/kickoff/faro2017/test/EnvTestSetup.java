package org.talend.kickoff.faro2017.test;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.catalina.Lifecycle;
import org.apache.catalina.startup.Tomcat;
import org.apache.cxf.feature.LoggingFeature;
import org.apache.meecrowave.Meecrowave;

public class EnvTestSetup implements Meecrowave.InstanceCustomizer {
    private final Collection<String> keys = new ArrayList<>();

    @Override
    public void accept(final Tomcat tomcat) {
        // activate to see http client requests
        // setProperty("http.client.features", LoggingFeature.class.getName());

        // clean up startup logs
        setProperty("org.apache.batchee.init.verbose", "false");

        // we handle it in tests to control the database state to avoid to do it at startup
        setProperty("provisioning.skip", "true");

        // we redirect remote calls to local resources to ensure it runs whatever network state is available
        final int port = tomcat.getConnector().getPort();
        setProperty("cboe.symbols.url", "http://localhost:" + port + "/publish/ScheduledTask/MktData/cboesymboldir2.csv");
        setProperty("yahoo.price.url", "http://localhost:" + port + "/v10/finance/quoteSummary/{symbol}?modules=financialData");

        // on shutdown we clean it all
        tomcat.getEngine().addLifecycleListener(event -> {
            if (event.getType().equals(Lifecycle.STOP_EVENT)) {
                keys.forEach(System::clearProperty);
            }
        });
    }

    private void setProperty(final String key, final String value) {
        System.setProperty(key, value);
        keys.add(key);
    }
}
