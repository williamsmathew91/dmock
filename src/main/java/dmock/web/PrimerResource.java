package dmock.web;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.IMap;
import dmock.model.MockData;
import dmock.util.KeyConcatUtil;
import dmock.web.model.PayloadGetMockData;
import dmock.web.model.PayloadMockData;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;

@Path("prime")
public class PrimerResource {
    private IMap<String, PayloadMockData> mockDataMap;

    @Inject
    private KeyConcatUtil keyConcatUtil;

    public PrimerResource() {
        keyConcatUtil = new KeyConcatUtil();

        mockDataMap = Hazelcast.newHazelcastInstance().getMap("mockMap");

        PayloadMockData payloadMockData = new PayloadMockData();
        payloadMockData.setMethod("GET");
        payloadMockData.setPath("PATH");
        MockData mockData = new MockData();
        mockData.setStatus((short) 200);

        payloadMockData.setMockData(mockData);
        setMock(payloadMockData);
    }

    @POST
    @Consumes("application/json")
    public void setMock(PayloadMockData payloadMockData) {
        String key = keyConcatUtil.concat(payloadMockData.getPath(), payloadMockData.getMethod());

        if(mockDataMap.get(key) != null) {
            return;
        }

        mockDataMap.put(key, payloadMockData);
    }

    @PUT
    @Consumes("application/json")
    public void updateMock(PayloadMockData payloadMockData) {
        String key = keyConcatUtil.concat(payloadMockData.getPath(), payloadMockData.getMethod());

        if(mockDataMap.get(key) == null) {
            return;
        }

        mockDataMap.put(key, payloadMockData);
    }

    @DELETE
    public void clearMocks() {
        mockDataMap.clear();
    }

    @GET
    @Path("/{encodedPath}/{method}")
    @Produces("application/json")
    public PayloadMockData getMock(@PathParam("encodedPath") String path, @PathParam("method") String method) {
        try {
            String decodedPath = URLDecoder.decode(path, "UTF-8");
            String key = keyConcatUtil.concat(decodedPath, method);
            return mockDataMap.get(key);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}