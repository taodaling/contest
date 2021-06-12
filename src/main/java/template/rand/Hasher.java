package template.rand;

public class Hasher {
    private final long time = System.nanoTime() + System.currentTimeMillis() * 31L;

    /**
     * Returns the 32 high bits of Stafford variant 4 mix64 function as int.
     */
    public int shuffle(long z) {
        z += time;
        z = (z ^ (z >>> 33)) * 0x62a9d9ed799705f5L;
        return (int) (((z ^ (z >>> 28)) * 0xcb24d0a5c88c35b3L) >>> 32);
    }

    public int hash(int x) {
        return shuffle(x);
    }

    public int hash(long x) {
        return shuffle(x);
    }

    public int hash(double x) {
        return shuffle(Double.doubleToRawLongBits(x));
    }

    public int hash(Object x) {
        return shuffle(x == null ? 0 : x.hashCode());
    }

}
