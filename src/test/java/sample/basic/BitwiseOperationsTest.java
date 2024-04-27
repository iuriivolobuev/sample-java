package sample.basic;

import org.junit.Test;

import static io.qala.datagen.RandomShortApi.integer;
import static org.junit.Assert.assertEquals;

public class BitwiseOperationsTest {
    @Test
    public void invertsAllBits() {
        assertAllBitsInverted(0);
        assertAllBitsInverted(-10);
        assertAllBitsInverted(10);
        assertAllBitsInverted(Integer.MIN_VALUE);
        assertAllBitsInverted(Integer.MAX_VALUE);
        assertAllBitsInverted(integer());
    }

    private static void assertAllBitsInverted(int value) {
        assertEquals(-1, value ^ BitwiseOperations.invertAllBits_v1(value));
        assertEquals(-1, value ^ BitwiseOperations.invertAllBits_v2(value));
        assertEquals(-1, value ^ BitwiseOperations.invertAllBits_v3(value));
    }
}