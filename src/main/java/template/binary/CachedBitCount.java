package template.binary;

public class CachedBitCount {
    private static final int BITS = 16;
    private static final int LIMIT = 1 << BITS;
    private static final int MASK = LIMIT - 1;
    private static final byte[] CACHE = new byte[LIMIT];

    static {
        for (int i = 1; i < LIMIT; i++) {
            CACHE[i] = (byte) (CACHE[i - (i & -i)] + 1);
        }
    }

    public static int bitCount(int x) {
        return CACHE[x & MASK] + CACHE[(x >>> 16) & MASK];
    }


    public static int bitCount(long x) {
        return CACHE[(int) (x & MASK)] + CACHE[(int) ((x >>> 16) & MASK)]
                + CACHE[(int) ((x >>> 32) & MASK)] + CACHE[(int) ((x >>> 48) & MASK)];
    }
}
