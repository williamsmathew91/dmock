package dmock.config;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import dmock.application.JaxRsApiApplication;
import dmock.model.MockData;
import dmock.util.KeyConcatUtil;
import dmock.web.PrimerResource;
import dmock.web.model.PayloadMockData;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.ext.RuntimeDelegate;
import java.util.Arrays;

@Configuration
@ComponentScan(basePackages = {"dmock.model.*", "dmock.web.model.*", "dmock.web.*"})
public class AppConfig {

    @Bean( destroyMethod = "shutdown" )
    public SpringBus cxf() {
        return new SpringBus();
    }

    @Bean
    PayloadMockData payloadMockData() {
        return new PayloadMockData();
    }

    @Bean
    public Server jaxRsServer() {
        System.out.println("------------------------------------------ HITTING JAXRSSERVER");
        JAXRSServerFactoryBean factory = RuntimeDelegate.getInstance().createEndpoint( jaxRsApiApplication(), JAXRSServerFactoryBean.class );
        factory.setServiceBeans( Arrays.< Object >asList(primerResource()) );
        factory.setAddress( "/" + factory.getAddress() );
        factory.setProviders( Arrays.< Object >asList( jsonProvider() ) );
        return factory.create();
    }

    @Bean
    public JaxRsApiApplication jaxRsApiApplication() {
        return new JaxRsApiApplication();
    }

    @Bean
    public KeyConcatUtil keyConcatUtil() {
        return new KeyConcatUtil();
    }

    @Bean
    public PrimerResource primerResource() {
        return new PrimerResource();
    }

    @Bean
    public JacksonJsonProvider jsonProvider() {
        return new JacksonJsonProvider();
    }
}
