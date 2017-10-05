package org.talend.kickoff.faro2017.batch.service;

import static java.util.Arrays.asList;
import static javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM_TYPE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class CboeService {
    @Inject
    @ConfigProperty(name = "cboe.symbols.url",
            defaultValue = "http://www.cboe.com/publish/ScheduledTask/MktData/cboesymboldir2.csv")
    private String cboeUrl;

    @Inject
    private Client client;

    public Iterator<String> findSymbols(final int maxSymbols) {
        try (final BufferedReader stream = new BufferedReader(new InputStreamReader(
                client.target(cboeUrl).request(APPLICATION_OCTET_STREAM_TYPE).get(InputStream.class),
                StandardCharsets.UTF_8))) {
            return asList(stream.lines()
                        .skip(2/*comment+header*/)
                        .limit(maxSymbols)
                        .map(line -> line.split(","))
                        .filter(columns -> columns.length > 2 && !columns[1].isEmpty())
                        .map(columns -> columns[1])
                        .toArray(String[]::new)).iterator();
        } catch (final IOException e) {
            throw new IllegalArgumentException("Can't connect to find symbols on " + cboeUrl, e);
        }
    }
}
