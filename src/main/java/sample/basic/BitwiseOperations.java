package sample.basic;

import org.apache.commons.lang3.StringUtils;

class BitwiseOperations {
    public static void main(String[] args) {
        printBits(0);
        printBits(1);
        printBits(2);
        printBits(-1);
        printBits(-2);
        printBits(-3);
        printBits(Integer.MAX_VALUE);
        printBits(Integer.MIN_VALUE);
    }

    public static int invertAllBits_v1(int value) {
        return ~value;
    }

    public static int invertAllBits_v2(int value) {
        //noinspection PointlessBitwiseExpression
        return value ^ -1;
    }

    public static int invertAllBits_v3(int value) {
        return -(value + 1);
    }

    private static void printBits(int value) {
        String bits = StringUtils.leftPad(Integer.toBinaryString(value), 32, "0");
        System.out.printf("%s (value=%d)%n", bits, value);
    }
}
