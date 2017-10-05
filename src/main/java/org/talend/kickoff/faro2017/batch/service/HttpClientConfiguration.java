package org.talend.kickoff.faro2017.batch.service;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.stream.Stream;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class HttpClientConfiguration {
    @Inject
    @ConfigProperty(name = "http.client.features", defaultValue = "")
    private String features;

    @Produces
    @ApplicationScoped
    public Client createClient() {
        final Client client = ClientBuilder.newClient();
        final Collection<Object> toRegister;
        if (features != null) {
            final ClassLoader loader = Thread.currentThread()
                                             .getContextClassLoader();
            toRegister = Stream.of(features.split(","))
                  .map(String::trim)
                  .filter(v -> !v.isEmpty())
                  .map(c -> {
                      try {
                          return loader.loadClass(c)
                                       .getConstructor()
                                       .newInstance();
                      } catch (final InstantiationException | ClassNotFoundException | NoSuchMethodException |
                              IllegalAccessException e) {
                          throw new IllegalArgumentException(e);
                      } catch (final InvocationTargetException e) {
                          throw new IllegalArgumentException(e.getTargetException());
                      }
                  })
                  .collect(toList());
        } else {
            toRegister = emptyList();
        }
        if (toRegister.isEmpty()) {
            return client;
        }
        return Client.class.cast(Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(), client.getClass().getInterfaces(),
                (proxy, method, args) -> {
                    try {
                        final Object invoke = method.invoke(client, args);
                        if (WebTarget.class.isInstance(invoke)) {
                            final WebTarget target = WebTarget.class.cast(invoke);
                            toRegister.forEach(target::register);
                        }
                        return invoke;
                    } catch (final InvocationTargetException ite) {
                        throw ite.getCause();
                    }
                }));
    }

    public void releaseClient(@Disposes final Client client) {
        client.close();
    }
}
