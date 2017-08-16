package com.mogujie.jarvis.rest;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.mogujie.jarvis.core.util.ConfigUtils;
import com.mogujie.jarvis.core.util.IPUtils;
import com.mogujie.jarvis.rest.controller.CharsetResponseFilter;

/**
 * RestServer工厂
 *
 * @author muming
 */
public class RestServerFactory {
    public static HttpServer createHttpServer() throws IOException {
        int port = ConfigUtils.getRestConfig().getInt("rest.http.port", 8080);
        String host = ConfigUtils.getRestConfig().getString("rest.http.hostname");
        if (host == null || host.trim().equals("")) {
            host = IPUtils.getIPV4Address();
        }
        URI baseUri = UriBuilder.fromUri("http://" + host + "/").port(port).build();
        ResourceConfig resourceConfig = new RestResourceConfig();
        resourceConfig.register(CharsetResponseFilter.class);
        return GrizzlyHttpServerFactory.createHttpServer(baseUri, resourceConfig);
    }
}
