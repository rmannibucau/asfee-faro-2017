package org.talend.kickoff.faro2017.batch.service;

import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.talend.kickoff.faro2017.service.model.Quote;

@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class Asyncs {
    public static <T> List<T> unwrap(final Collection<?> futures) {
        final Collection<CompletableFuture<Quote>> out = Collection.class.cast(futures);

        try {
            CompletableFuture.allOf(out.toArray(new CompletableFuture[out.size()]))
                             .exceptionally(error -> {
                                 log.error(error.getMessage(), error);
                                 return null;
                             })
                             .get(); // in real life use a timeout with @ConfigProperty values
        } catch (final InterruptedException e) {
            Thread.interrupted();
            throw new IllegalStateException(e);
        } catch (final ExecutionException e) {
            throw new IllegalStateException(e);
        }

        return (List<T>) out.stream()
              .filter(f -> !f.isCompletedExceptionally())
              .map(f -> {
                  try {
                      return f.get();
                  } catch (final InterruptedException e) {
                      Thread.interrupted();
                      throw new IllegalStateException(e);
                  } catch (final ExecutionException e) {
                      log.error(e.getMessage(), e);
                      throw new IllegalStateException(e);
                  }
              }).collect(toList());
    }
}
