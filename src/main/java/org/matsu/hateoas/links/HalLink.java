package org.matsu.hateoas.links;

import javax.ws.rs.HttpMethod;

public class HalLink {
    private String href;
    private String rel;
    private HttpMethod type;



    public String getHref() {
        return href;
    }
    public void setHref(String href) {
        this.href = href;
    }
    public String getRel() {
        return rel;
    }
    public void setRel(String rel) {
        this.rel = rel;
    }
    public HttpMethod getType() {
        return type;
    }
    public void setType(HttpMethod type) {
        this.type = type;
    }

}
