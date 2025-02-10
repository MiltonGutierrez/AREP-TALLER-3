package edu.escuelaing.arep.taller3.http;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String path = null;
    private String query = null; 
    private Map<String, String> queryParams = new HashMap<>();

    public HttpRequest(String path, String query) {
        this.path = path;
        this.query = query;
        setQueryParams();
    }

    public String getPath() {
        return path;
    }

    public String getQuery() {
        return query;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams() {
        if (query != null) {
            String[] params = query.split("&");
            Arrays.toString(params);
            for (String param : params) {
                String[] keyValue = param.split("=");
                queryParams.put(keyValue[0], keyValue.length <= 1 ? "" : keyValue[1]);
            }
        }
    }
}
