package template.rand;

import template.math.DigitUtils;

public class HashUtils {
    public static int hash(long x) {
        long high = DigitUtils.highBit(x);
        long low = DigitUtils.lowBit(x);
        return (int) ((high * 31 + low) % ((int) 1e9 + 7));
    }
}
