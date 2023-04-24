package ch.uzh.ifi.hase.soprafs23.entityDB;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OutlineTest {

    private Outline outline;

    @BeforeEach
    void setUp() {
        outline = new Outline();
    }

    @Test
    void testOutlineId() {
        outline.setOutlineId(1L);
        assertNotNull(outline.getOutlineId());
        assertEquals(1L, outline.getOutlineId());
    }

    @Test
    void testOutline() {
        String testOutline = "This is a test outline.";
        outline.setOutline(testOutline);
        assertNotNull(outline.getOutline());
        assertEquals(testOutline, outline.getOutline());
    }
}
