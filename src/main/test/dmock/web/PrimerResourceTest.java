package dmock.web;

import com.hazelcast.core.IMap;
import dmock.model.MockData;
import dmock.util.KeyConcatUtil;
import dmock.web.model.PayloadGetMockData;
import dmock.web.model.PayloadMockData;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class PrimerResourceTest {
    private PrimerResource primerResource;

    @Mock
    private IMap<String, MockData> mapMock;

    @Mock
    private KeyConcatUtil keyConcatUtilMock;


    @Before
    public void setUp() {
        initMocks(this);

        primerResource = new PrimerResource();

        Whitebox.setInternalState(primerResource, "mockDataMap", mapMock);
        Whitebox.setInternalState(primerResource, "keyConcatUtil", keyConcatUtilMock);
    }

    @Test
    public void setShouldPutCorrectDataInMockDataMap() {
        final String hashKey = "hashKey";

        PayloadMockData payloadMockData = new PayloadMockData();
        final String somePath = "somePath";
        final String someMethod = "someMethod";

        payloadMockData.setPath(somePath);
        payloadMockData.setMethod(someMethod);

        when(keyConcatUtilMock.concat(somePath, someMethod)).thenReturn(hashKey);

        primerResource.setMock(payloadMockData);

        verify(mapMock).put(hashKey, payloadMockData.getMockData());
    }

    @Test
    public void getMockShouldReturnCorrectMockFromMap() {
        final String hashKey = "hashKey";
        final String path = "somePath";
        final String method = "someMethod";

        PayloadGetMockData payloadGetMockData = new PayloadGetMockData();
        payloadGetMockData.setPath(path);
        payloadGetMockData.setMethod(method);

        MockData mockData = new MockData();
        mockData.setBody("body");
        mockData.setDelay(1);
        mockData.setStatus((short) 500);

        when(keyConcatUtilMock.concat(path, method)).thenReturn(hashKey);
        when(mapMock.get(hashKey)).thenReturn(mockData);

        final MockData primedMock = primerResource.getMock(payloadGetMockData);

        assertEquals(mockData, primedMock);
    }

    @Test
    public void clearShouldEmptyMap() {
        primerResource.clearMocks();

        verify(mapMock).clear();
    }
}
