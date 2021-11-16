package template.rand;

public class FastUniversalHashFunction1 implements HashFunction {
    static final FastUniversalHashFunction1 INSTANCE = new FastUniversalHashFunction1();
    private final long time = System.nanoTime() + System.currentTimeMillis() * 31L;

    private FastUniversalHashFunction1() {
    }

    @Override
    public long f(long z) {
        z += time;
        z = (z ^ (z >>> 33)) * 0x62a9d9ed799705f5L;
        z = (((z ^ (z >>> 28)) * 0xcb24d0a5c88c35b3L) >>> 32);
        return z;
    }

    @Override
    public HashFunction upgrade() {
        return FastUniversalHashFunction2.INSTANCE;
//        FastUniversalHashFunction0.numberOfInstance--;
//        return new UniversalHashFunction();
    }
}
