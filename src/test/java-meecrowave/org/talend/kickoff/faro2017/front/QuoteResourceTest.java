package org.talend.kickoff.faro2017.front;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.junit.Assert.assertEquals;
import static org.junit.rules.RuleChain.outerRule;

import javax.inject.Inject;
import javax.ws.rs.client.Client;

import org.apache.meecrowave.Meecrowave;
import org.apache.meecrowave.junit.MonoMeecrowave;
import org.apache.meecrowave.testing.ConfigurationInject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.talend.kickoff.faro2017.front.model.QuoteModel;
import org.talend.kickoff.faro2017.front.model.QuotePage;
import org.talend.kickoff.faro2017.test.InjectorRule;
import org.talend.kickoff.faro2017.test.VolatileProvisioning;

@RunWith(MonoMeecrowave.Runner.class)
public class QuoteResourceTest {
    private final MonoMeecrowave.Rule meecrowave = new MonoMeecrowave.Rule();

    @Rule
    public final TestRule injector = outerRule(meecrowave)
            .around(new InjectorRule(this))
            .around(new VolatileProvisioning());

    @Inject
    private Client client;

    @Test
    public void findPage() {
        final QuotePage page = client.target("http://localhost:" + meecrowave.getConfiguration().getHttpPort())
              .path("api/quote")
              .queryParam("from", 4)
              .queryParam("to", 9)
              .request(APPLICATION_JSON_TYPE)
              .get(QuotePage.class);
        assertEquals(5, page.getItems().size());
        assertEquals(10, page.getTotal());
        assertEquals(asList("TLND", "TWOU", "VNET", "WBAI", "WUBA"), page.getItems().stream().map(QuoteModel::getName).collect(toList()));
        assertEquals(asList(41.94, 58.2, 6.67, 11.53, 65.35), page.getItems().stream().map(QuoteModel::getPrice).collect(toList()));
    }

    @Test
    public void findQuote() {
        assertEquals(41.94, client.target("http://localhost:" + meecrowave.getConfiguration().getHttpPort())
                                .path("api/quote/{symbol}")
                                .resolveTemplate("symbol", "TLND")
                                .request(APPLICATION_JSON_TYPE)
                                .get(QuoteModel.class).getPrice(), 0);
    }
}
