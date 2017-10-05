package org.talend.kickoff.faro2017.front;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

import java.util.Properties;
import java.util.function.Function;
import javax.batch.operations.JobOperator;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.talend.kickoff.faro2017.front.model.QuoteModel;
import org.talend.kickoff.faro2017.front.model.QuotePage;
import org.talend.kickoff.faro2017.service.model.Quote;

@Path("quote")
@ApplicationScoped
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class QuoteResource {
    @Inject // use deltaspike-data if you want to have more fun but doubt you'll get time in 1h ;)
    private EntityManager em;

    @Inject
    private JobOperator jobOperator;

    private final Function<Quote, QuoteModel> mapper = quote -> new QuoteModel(quote.getSymbol(), quote.getPrice());

    @GET
    public QuotePage findPage(@QueryParam("from") @DefaultValue("0") final int from,
                              @QueryParam("to") @DefaultValue("4") final int to) {
        if (to < from || to < 0 || from < 0 || (to - from) > 100) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST); // if you have time add an ErrorPayload
        }
        return new QuotePage(
                em.createNamedQuery("Quote.findAll", Quote.class)
                    .setFirstResult(from)
                    .setMaxResults(to - from)
                    .getResultList().stream()
                    .map(mapper)
                    .collect(toList()),
                em.createNamedQuery("Quote.countAll", Number.class)
                    .getSingleResult()
                    .longValue());
    }

    @GET
    @Path("{symbol}")
    public QuoteModel findQuote(@PathParam("symbol") final String symbol) {
        return ofNullable(em.find(Quote.class, symbol))
                .map(mapper)
                .orElseThrow(() -> new NotFoundException(symbol + " not found"));
    }

    @GET
    @Path("provisining")
    @Produces(TEXT_PLAIN)
    public long provision() {
        return jobOperator.start("quote", new Properties());
    }
}
