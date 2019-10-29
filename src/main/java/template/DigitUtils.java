package template;

import java.util.Arrays;

public class DigitUtils {
    private DigitUtils() {}

    private static final long[] DIGIT_VALUES = new long[19];

    static {
        DIGIT_VALUES[0] = 1;
        for (int i = 1; i < 19; i++) {
            DIGIT_VALUES[i] = DIGIT_VALUES[i - 1] * 10;
        }
    }

    /**
     * Get the digit on the i-th position while i start from 0
     */
    public static int digitOn(long x, int i) {
        if (x < 0) {
            return digitOn(-x, i);
        }
        x /= DIGIT_VALUES[i];
        return (int) (x % 10);
    }

    public static long setDigitOn(long x, int i, int newDigit) {
        if (x < 0) {
            return -setDigitOn(-x, i, newDigit);
        }
        return x + (newDigit - digitOn(x, i)) * DIGIT_VALUES[i];
    }

    /**
     * return floor(a / b) while b is positive
     */
    public static long floorDiv(long a, long b) {
        return a < 0 ? -ceilDiv(-a, b) : a / b;
    }

    /**
     * return floor(a / b) while b is positive
     */
    public static int floorDiv(int a, int b) {
        return a < 0 ? -ceilDiv(-a, b) : a / b;
    }

    /**
     * return ceil(a / b) while b is positive
     */
    public static long ceilDiv(long a, long b) {
        return a < 0 ? -floorDiv(-a, b) : (a + b - 1) / b;
    }

    public static boolean isMultiplicationOverflow(long a, long b, long limit) {
        if (limit < 0) {
            limit = -limit;
        }
        if (a < 0) {
            a = -a;
        }
        if (b < 0) {
            b = -b;
        }
        if (a == 0 || b == 0) {
            return false;
        }
        return a > limit / b;
    }

    public static long mul(long a, long b, long limit, long overflowVal) {
        return isMultiplicationOverflow(a, b, limit) ? overflowVal : a * b;
    }

    public static long mul(long a, long b, long overflowVal) {
        return mul(a, b, Long.MAX_VALUE, overflowVal);
    }

    /**
     * return ceil(a / b) while b is positive
     */
    public static int ceilDiv(int a, int b) {
        return a < 0 ? -floorDiv(-a, b) : (a + b - 1) / b;
    }

    public static long asLong(int high, int low) {
        return (((long) high) << 32) | low;
    }

    public static int highBit(long x) {
        return (int) (x >> 32);
    }

    public static int lowBit(long x) {
        return (int) x;
    }

    public static boolean isOdd(int x) {
        return (x & 1) == 1;
    }

    public static boolean isOdd(long x) {
        return (x & 1) == 1;
    }

    public static boolean isEven(int x) {
        return (x & 1) == 0;
    }

    public static boolean isEven(long x) {
        return (x & 1) == 0;
    }

    public static int mod(long x, int mod) {
        x %= mod;
        if (x < 0) {
            x += mod;
        }
        return (int) x;
    }

    public static int mod(int x, int mod) {
        x %= mod;
        if (x < 0) {
            x += mod;
        }
        return x;
    }

    public static long mod(long x, long mod) {
        x %= mod;
        if (x < 0) {
            x += mod;
        }
        return x;
    }

    /**
     * Bit operations
     */
    public static class BitOperator {
        public int bitAt(int x, int i) {
            return (x >> i) & 1;
        }

        public int bitAt(long x, int i) {
            return (int) ((x >> i) & 1);
        }

        public int setBit(int x, int i, boolean v) {
            if (v) {
                x |= 1 << i;
            } else {
                x &= ~(1 << i);
            }
            return x;
        }

        public long setBit(long x, int i, boolean v) {
            if (v) {
                x |= 1L << i;
            } else {
                x &= ~(1L << i);
            }
            return x;
        }

        public long swapBit(long x, int i, int j) {
            int bi = bitAt(x, i);
            int bj = bitAt(x, j);
            x = setBit(x, i, bj == 1);
            x = setBit(x, j, bi == 1);
            return x;
        }

        public int swapBit(int x, int i, int j) {
            int bi = bitAt(x, i);
            int bj = bitAt(x, j);
            x = setBit(x, i, bj == 1);
            x = setBit(x, j, bi == 1);
            return x;
        }

        /**
         * Determine whether x is subset of y
         */
        public boolean subset(long x, long y) {
            return intersect(x, y) == x;
        }

        /**
         * Merge two set
         */
        public long merge(long x, long y) {
            return x | y;
        }

        public long intersect(long x, long y) {
            return x & y;
        }

        public int intersect(int x, int y) {
            return x & y;
        }

        public long differ(long x, long y) {
            return x - intersect(x, y);
        }

        public int differ(int x, int y) {
            return x - intersect(x, y);
        }
    }

    /**
     * Log operations
     */
    public static class Log2 {
        public int ceilLog(int x) {
            return 32 - Integer.numberOfLeadingZeros(x - 1);
        }

        public int floorLog(int x) {
            return 31 - Integer.numberOfLeadingZeros(x);
        }

        public int ceilLog(long x) {
            return 64 - Long.numberOfLeadingZeros(x - 1);
        }

        public int floorLog(long x) {
            return 63 - Long.numberOfLeadingZeros(x);
        }
    }

    /**
     * Log operations
     */
    public static class CachedLog2 {
        public CachedLog2(int n) {
            cache = new int[n + 1];
            int b = 0;
            for (int i = 0; i <= n; i++) {
                while ((1 << (b + 1)) <= i) {
                    b++;
                }
                cache[i] = b;
            }
        }

        private int[] cache;
        private Log2 log2;

        public int ceilLog(int x) {
            int ans = floorLog(x);
            if ((1 << ans) < x) {
                x++;
            }
            return x;
        }

        public int floorLog(int x) {
            if (x >= cache.length) {
                return log2.floorLog(x);
            }
            return cache[x];
        }
    }

    public static class DigitBase {
        private long[] pow;
        private long base;

        public DigitBase(long base) {
            if (base <= 1) {
                throw new IllegalArgumentException();
            }
            this.base = base;
            LongList ll = new LongList(64);
            ll.add(1);
            while (!isMultiplicationOverflow(ll.tail(), base, Long.MAX_VALUE)) {
                ll.add(ll.tail() * base);
            }
            pow = ll.toArray();
        }

        public long valueOfBit(int i) {
            return pow[i];
        }

        public int getBit(long x, int i) {
            return (int) (x / pow[i] % base);
        }

        public long setBit(long x, int i, long val) {
            return x + (val - getBit(x, i)) * pow[i];
        }

        public int bitCount() {
            return pow.length;
        }

        public int[] decompose(long x) {
            return decompose(x, new int[bitCount()]);
        }

        public int[] decompose(long x, int[] ans) {
            for (int i = 0; i < ans.length; i++) {
                ans[i] = (int)(x % base);
                x /= base;
            }
            return ans;
        }

        public long compose(int[] bits) {
            if (bits.length > bitCount()) {
                throw new IllegalArgumentException();
            }
            long ans = 0;
            for (int i = bits.length - 1; i >= 0; i--) {
                ans = ans * base + bits[i];
            }
            return ans;
        }

        public String toString(long x) {
            int[] bits = decompose(x);
            return Arrays.toString(bits);
        }
    }
}
