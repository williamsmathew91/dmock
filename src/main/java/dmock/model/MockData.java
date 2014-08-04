package dmock.model;

import java.util.Map;

public class MockData {
    private short status;
    private Map<String, String> headers;
    private String body;
    private long delay;

    public MockData() {

    }

    public long getDelay() {
        return delay;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }
}
