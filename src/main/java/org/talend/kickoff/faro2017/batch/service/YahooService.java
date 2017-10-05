package org.talend.kickoff.faro2017.batch.service;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;

import org.apache.deltaspike.core.api.future.Futureable;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.talend.kickoff.faro2017.service.model.Quote;

@Slf4j
@ApplicationScoped
public class YahooService {
    @Inject
    @ConfigProperty(name = "yahoo.price.url",
            defaultValue = "https://query1.finance.yahoo.com/v10/finance/quoteSummary/{symbol}?modules=financialData")
    private String yahooUrl;

    @Inject
    private Client client;

    private final Throwable error = new Throwable() {{
        setStackTrace(new StackTraceElement[0]);
    }};

    @Futureable
    public CompletionStage<Quote> find(final String symbol) {
        final CompletableFuture<Quote> result = new CompletableFuture<>();
        try {
            final Data data = client.target(yahooUrl)
                                    .resolveTemplate("symbol", symbol)
                                    .request(APPLICATION_JSON_TYPE)
                                    .get(Data.class);
            if (!data.hasPrice()) {
                log.warn("Can't retrieve '{}', {}", symbol, data.getQuoteSummary() != null ? data.getQuoteSummary()
                                                                                                 .getError() : "");
                result.completeExceptionally(error);
            } else {
                result.complete(new Quote(symbol, data.getQuoteSummary()
                                                      .getResult()
                                                      .get(0)
                                                      .getFinancialData()
                                                      .getCurrentPrice()
                                                      .getRaw()));
            }
        } catch (final WebApplicationException error) {
            log.warn(
                    "Error getting '" + symbol + "': " + error.getMessage() +
                            " (HTTP " + (error.getResponse() == null ? "-" : error.getResponse().getStatus()) + ")");
            result.completeExceptionally(error);
        }

        return result;
    }

    @lombok.Data
    public static class Data {
        private QuoteSummary quoteSummary;

        boolean hasPrice() {
            return !(getQuoteSummary() == null || getQuoteSummary().getError() != null ||
                    getQuoteSummary().getResult() == null || getQuoteSummary().getResult().isEmpty() ||
                    getQuoteSummary().getResult().get(0).getFinancialData() == null ||
                    getQuoteSummary().getResult().get(0).getFinancialData().getCurrentPrice() == null);
        }
    }

    @lombok.Data
    public static class Error {
        private String code;
        private String description;
    }

    @lombok.Data
    public static class QuoteSummary {
        private Error error;
        private List<Result> result;
    }

    @lombok.Data
    public static class Result {
        private FinancialData financialData;
    }

    @lombok.Data
    public static class FinancialData {
        private Price currentPrice;
    }

    @lombok.Data
    public static class Price {
        private double raw;
    }
}
