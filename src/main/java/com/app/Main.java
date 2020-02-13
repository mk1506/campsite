package com.app;

import com.app.config.EventListenerScanner;
import com.app.config.HandlerScanner;
import com.app.config.JettyModule;
import com.app.config.RestEasyModule;
import com.app.data.DataModule;
import com.app.resource.ResourceModule;
import com.app.service.ServiceModule;
import com.app.swagger.SwaggerModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    static final String APPLICATION_PATH = "/api";
    static final String CONTEXT_ROOT = "/";
    private static final int DEFAULT_PORT = 5000;

    private final GuiceFilter filter;
    private final EventListenerScanner eventListenerScanner;
    private final HandlerScanner handlerScanner;

    // Guice can work with both javax and guice annotations.
    @Inject
    public Main(GuiceFilter filter, EventListenerScanner eventListenerScanner, HandlerScanner handlerScanner) {
        this.filter = filter;
        this.eventListenerScanner = eventListenerScanner;
        this.handlerScanner = handlerScanner;
    }

    public static void main(String[] args) throws Exception {

        try {

            final Injector injector = Guice.createInjector(new JettyModule(), new RestEasyModule(APPLICATION_PATH),
                    new ResourceModule(),new DataModule(),new ServiceModule(), new SwaggerModule(APPLICATION_PATH));

            injector.getInstance(Main.class).run();

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void run() throws Exception{
        final Server server = new Server(DEFAULT_PORT);

        final ServletContextHandler context = new ServletContextHandler(server, CONTEXT_ROOT);

        // Add the GuiceFilter (all requests will be routed through GuiceFilter).
        FilterHolder filterHolder = new FilterHolder(filter);
        context.addFilter(filterHolder, APPLICATION_PATH + "/*", null);

        //set default path
        final ServletHolder defaultServlet = new ServletHolder(new DefaultServlet());
        context.addServlet(defaultServlet, CONTEXT_ROOT);

        //swagger routing
        String resourceBasePath = Main.class.getResource("/swagger-ui").toExternalForm();
        context.setResourceBase(resourceBasePath);
        context.setWelcomeFiles(new String[] { "index.html" });

        //event listener and handlers binding
        eventListenerScanner.accept((listener) -> {
            context.addEventListener(listener);
        });

        final HandlerCollection handlers = new HandlerCollection();
        handlers.addHandler(server.getHandler());

        handlerScanner.accept((handler) -> {
            handlers.addHandler(handler);
        });

        server.setHandler(handlers);
        server.start();
        logger.warn("application started.");
        server.join();
    }

}
