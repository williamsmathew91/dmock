package dmock.web.filter;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.IMap;
import dmock.model.MockData;
import dmock.util.KeyConcatUtil;
import dmock.web.model.PayloadMockData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class MockMatchingFilter implements ContainerRequestFilter {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private IMap<String, PayloadMockData> mockMap;
    private KeyConcatUtil keyConcatUtil;


    public MockMatchingFilter() {
        mockMap = Hazelcast.newHazelcastInstance().getMap("mockMap");
        keyConcatUtil = new KeyConcatUtil();
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        long workloadStartTime = System.currentTimeMillis();

        String path = requestContext.getUriInfo().getPath();

        if(path.contains("prime")) {
            return;
        }

        String method = requestContext.getMethod();

        String mockKey = keyConcatUtil.concat(path, method);
        PayloadMockData payloadMockData = mockMap.get(mockKey);

        if(payloadMockData == null) {
            Response mockNotSetResponse = Response.status(503).entity("Mock not set.").build();
            requestContext.abortWith(mockNotSetResponse);
        }

        MockData mockData = payloadMockData.getMockData();

        Response.ResponseBuilder mockResponseBuilder = Response.status(mockData.getStatus()).entity(mockData.getBody());

        Iterator<Map.Entry<String,String>> mockHeadersI = mockData.getHeaders().entrySet().iterator();
        while(mockHeadersI.hasNext()) {
            Map.Entry<String, String> mockHeader = mockHeadersI.next();
            mockResponseBuilder.header(mockHeader.getKey(), mockHeader.getValue());
        }

        Response mockResponse = mockResponseBuilder.build();

        long workloadEndTime = System.currentTimeMillis();

        try {
            Thread.sleep(mockData.getDelay() - (workloadEndTime - workloadStartTime));
        } catch (InterruptedException e) {}
        finally {
            requestContext.abortWith(mockResponse);
        }

    }
}
