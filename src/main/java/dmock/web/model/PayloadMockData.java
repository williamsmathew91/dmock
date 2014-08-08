package dmock.web.model;

import dmock.model.MockData;

import java.io.Serializable;

public class PayloadMockData implements Serializable{
    private String path;
    private String method;

    private MockData mockData;


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public MockData getMockData() {
        return mockData;
    }

    public void setMockData(MockData mockData) {
        this.mockData = mockData;
    }
}
