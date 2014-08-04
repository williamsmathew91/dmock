package dmock.jetty;


import org.apache.cxf.transport.servlet.CXFServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class EmbeddedJetty {
    public void startServer() {
        try {
            Server server = new Server();
            ServerConnector c = new ServerConnector(server);
            c.setIdleTimeout(1000);
            c.setAcceptQueueSize(10);
            c.setPort(8080);
            c.setHost("localhost");
            ServletContextHandler handler = new ServletContextHandler(server,
                    "/", true, false);
            final CXFServlet servlet = new CXFServlet();
            ServletHolder servletHolder = new ServletHolder(
                    servlet);
            handler.addServlet(servletHolder, "/dmock");
            server.addConnector(c);
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
