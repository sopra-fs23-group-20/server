package ch.uzh.ifi.hase.soprafs23.exceptionTests;

import ch.uzh.ifi.hase.soprafs23.exceptions.GlobalExceptionAdvice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ExceptionsTest {

    private CustomGlobalExceptionAdvice globalExceptionAdvice;

    @Mock
    private Logger logger;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        globalExceptionAdvice = new CustomGlobalExceptionAdvice(logger);
    }

    @Test
    public void testHandleConflict_IllegalArgumentException() {
        RuntimeException ex = new IllegalArgumentException("test exception");
        WebRequest request = mock(WebRequest.class);

        ResponseEntity<Object> response = globalExceptionAdvice.handleConflict(ex, request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("This should be application specific", response.getBody());
    }

    @Test
    public void testHandleConflict_IllegalStateException() {
        RuntimeException ex = new IllegalStateException("test exception");
        WebRequest request = mock(WebRequest.class);

        ResponseEntity<Object> response = globalExceptionAdvice.handleConflict(ex, request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("This should be application specific", response.getBody());
    }


    @Test
    public void testHandleException() {
        Exception ex = new Exception("test exception");

        ResponseStatusException response = globalExceptionAdvice.handleException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatus());
        assertEquals("test exception", response.getReason());
        assertEquals(ex, response.getCause());
        verify(logger).error("Default Exception Handler -> caught:", ex);
    }

    private static class CustomGlobalExceptionAdvice extends GlobalExceptionAdvice {

        private final Logger logger;

        public CustomGlobalExceptionAdvice(Logger logger) {
            this.logger = logger;
        }

        @Override
        public ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
            return super.handleConflict(ex, request);
        }

        @Override
        public ResponseStatusException handleTransactionSystemException(Exception ex, HttpServletRequest request) {
            logger.error("Request: {} raised {}", request.getRequestURL(), ex);
            return new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage(), ex);
        }

        @Override
        public ResponseStatusException handleException(Exception ex) {
            logger.error("Default Exception Handler -> caught:", ex);
            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }

    }
}
