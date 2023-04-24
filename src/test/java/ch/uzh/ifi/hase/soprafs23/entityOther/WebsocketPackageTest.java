package ch.uzh.ifi.hase.soprafs23.entityOther;

import ch.uzh.ifi.hase.soprafs23.constant.WebsocketType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WebsocketPackageTest {
    private WebsocketPackage websocketPackage;

    @BeforeEach
    void setUp() {
        websocketPackage = new WebsocketPackage();
    }

    @Test
    void testParameterizedConstructor() {
        WebsocketType type = WebsocketType.CATEGORYUPDATE;
        String payload = "Test payload";
        WebsocketPackage websocketPackage = new WebsocketPackage(type, payload);

        assertEquals(type, websocketPackage.getType());
        assertEquals(payload, websocketPackage.getPayload());
    }

    @Test
    void testDefaultConstructor() {
        assertNull(websocketPackage.getType());
        assertNull(websocketPackage.getPayload());
    }

    @Test
    void testGetTypeAndSetType() {
        websocketPackage.setType(WebsocketType.CATEGORYUPDATE);
        assertEquals(WebsocketType.CATEGORYUPDATE, websocketPackage.getType());

        websocketPackage.setType(WebsocketType.GAMESTATEUPDATE);
        assertEquals(WebsocketType.GAMESTATEUPDATE, websocketPackage.getType());
    }

    @Test
    void testGetPayloadAndSetPayload() {
        String payload = "Test payload";
        websocketPackage.setPayload(payload);
        assertEquals(payload, websocketPackage.getPayload());

        Integer intPayload = 42;
        websocketPackage.setPayload(intPayload);
        assertEquals(intPayload, websocketPackage.getPayload());
    }
}
