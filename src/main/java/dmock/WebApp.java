package dmock;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;

import java.net.URL;
import java.security.ProtectionDomain;

public class WebApp {
    public static void main(String[] args) throws Exception
    {

        final String contextPath = "/";

        ProtectionDomain protectionDomain = WebApp.class.getProtectionDomain();
        URL location = protectionDomain.getCodeSource().getLocation();

        // create a web app and configure it to the root context of the server
        WebAppContext webapp = new WebAppContext();
        webapp.setDescriptor("WEB-INF/web.xml");
        webapp.setContextPath(contextPath);
        webapp.setWar(location.toExternalForm());
        webapp.setParentLoaderPriority(true);

        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);
        connector.setIdleTimeout(30000);

        connector.setAcceptQueueSize(20*3000);//time in seconds we want it to recover * max requests per second we can handle per node
        server.setConnectors(new Connector[] { connector });

        server.setHandler(webapp);
        server.start();
        server.join();
    }
}