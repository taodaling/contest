package template.binary;

public class FastBitCount2 {
    private static byte[] size = new byte[1 << 16];

    static {
        for (int i = 0; i < size.length; i++) {
            size[i] = (byte) Integer.bitCount(i);
        }
    }


    static final int MASK = (1 << 16) - 1;
    public static int count(int x) {
        return size[x & MASK] + size[(x >>> 16) & MASK];
    }
    public static int count(long x) {
        return count((int) x) + count((int) (x >>> 32));
    }

}
