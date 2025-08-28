package sample.basic;

import org.junit.Test;

import java.util.Set;

import static io.qala.datagen.RandomShortApi.integer;
import static org.junit.Assert.assertEquals;
import static sample.basic.BitwiseOperations.*;

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

    @Test
    public void isPowerOfTwo() {
        assertOnlySpecifiedValuesIsPowerOfTwo(1500, Set.of(1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024));
    }

    private static void assertAllBitsInverted(int value) {
        assertEquals(-1, value ^ invertAllBits_v1(value));
        assertEquals(-1, value ^ invertAllBits_v2(value));
        assertEquals(-1, value ^ invertAllBits_v3(value));
    }

    private static void assertOnlySpecifiedValuesIsPowerOfTwo(int maxToCheck, Set<Integer> values) {
        for (int i = 1; i <= maxToCheck; i++) {
            boolean expected = values.contains(i);
            assertEquals("i=%d, expected=%s, actual=%s".formatted(i, expected, isPowerOfTwo_v1(i)), expected, isPowerOfTwo_v1(i));
            assertEquals("i=%d, expected=%s, actual=%s".formatted(i, expected, isPowerOfTwo_v2(i)), expected, isPowerOfTwo_v2(i));
        }
    }
}