package main.java.com.acme.jetty.problem;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestServer.class);

    public static void main(String[] args) throws Exception {
        Server server = new Server();

        server.addConnector(createConnector(server, 10080));
        server.addConnector(createSecureConnector(server, 10443));

        ServletContextHandler servletContextHandler = new ServletContextHandler(
                ServletContextHandler.SECURITY | ServletContextHandler.SESSIONS);
        servletContextHandler.setContextPath("/");
        servletContextHandler.addServlet(new ServletHolder(new EchoServlet()), "/*");
        server.setHandler(servletContextHandler);

        try {
            server.start();
        } catch (Exception e) {
            LOGGER.error("Unable to start HTTP server.", e);
        }

    }

    private static Connector createConnector(Server server, int port) {
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        return connector;
    }

    private static Connector createSecureConnector(Server server, int port) throws KeyManagementException,
            NoSuchAlgorithmException {
        ServerConnector connector = new ServerConnector(server, createSslContextFactory());
        connector.setPort(port);
        connector.setSoLingerTime(1);
        return connector;
    }

    private static SslContextFactory createSslContextFactory() {
        SslContextFactory sslContextFactory = new SslContextFactory(true);
        sslContextFactory.setNeedClientAuth(false);
        sslContextFactory.setWantClientAuth(true);
        sslContextFactory.setKeyStorePath("etc/server.jks");
        sslContextFactory.setKeyStorePassword("secret");
        return sslContextFactory;
    }

}
