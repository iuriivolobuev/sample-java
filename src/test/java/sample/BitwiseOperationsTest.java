package sample;

import org.junit.jupiter.api.Test;

import static io.qala.datagen.RandomShortApi.integer;
import static org.assertj.core.api.Assertions.assertThat;

class BitwiseOperationsTest {
    @Test
    void invertsAllBits() {
        assertAllBitsInverted(0);
        assertAllBitsInverted(-10);
        assertAllBitsInverted(10);
        assertAllBitsInverted(Integer.MIN_VALUE);
        assertAllBitsInverted(Integer.MAX_VALUE);
        assertAllBitsInverted(integer());
    }

    private static void assertAllBitsInverted(int value) {
        assertThat(value ^ BitwiseOperations.invertAllBits_v1(value)).isEqualTo(-1);
        assertThat(value ^ BitwiseOperations.invertAllBits_v2(value)).isEqualTo(-1);
        assertThat(value ^ BitwiseOperations.invertAllBits_v3(value)).isEqualTo(-1);
    }
}