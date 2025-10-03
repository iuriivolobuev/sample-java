package app;

import java.time.OffsetDateTime;

import static org.junit.Assert.*;

public class TestUtils {
    public static void assertDatesEqual(OffsetDateTime expected, OffsetDateTime actual) {
        if (expected == null) {
            assertNull(actual);
        } else {
            assertNotNull(actual);
            assertEquals(expected.toInstant(), actual.toInstant());
        }
    }
}
