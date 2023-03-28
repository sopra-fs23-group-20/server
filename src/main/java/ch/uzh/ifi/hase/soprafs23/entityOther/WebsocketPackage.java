package ch.uzh.ifi.hase.soprafs23.entityOther;

import ch.uzh.ifi.hase.soprafs23.constant.WebsocketType;

public class WebsocketPackage {
    private WebsocketType type;
    private Object payload;

    public WebsocketPackage(WebsocketType type, Object payload) {
        this.type = type;
        this.payload = payload;
    }

    public WebsocketPackage() {
    }

    public WebsocketType getType() {
        return type;
    }

    public void setType(WebsocketType type) {
        this.type = type;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }
}
