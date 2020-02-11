package managesys;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class RequestUrl {
    private String url = "";
    private Map<String, String> params = new HashMap<String, String>();

    public String getRequestUrl() {
        return url + "?" + getParams();
    }

    private String getParams() {
        return params.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));
    }

    private RequestUrl(Builder builder) {
        this.url = builder.url;
        this.params = builder.params;
    }

    public static class Builder {
        private String url = "";
        private Map<String, String> params = new HashMap<String, String>();

        public static Builder Builder() {
            return new Builder();
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder param(String name, String value) {
            this.params.put(name, value);
            return this;
        }

        public RequestUrl build() {
            return new RequestUrl(this);
        }
    }
}
