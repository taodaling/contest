package template.rand;

public class FastUniversalHashFunction0 implements HashFunction{
    public static long numberOfInstance = 0;

    public static FastUniversalHashFunction0 getInstance() {
        numberOfInstance++;
        return INSTANCE;
    }

    static final FastUniversalHashFunction0 INSTANCE = new FastUniversalHashFunction0();

    private FastUniversalHashFunction0(){}

    @Override
    public long f(long x) {
        return x ^ (x >>> 32);
    }

    @Override
    public HashFunction upgrade() {
        return FastUniversalHashFunction4.INSTANCE;
//        FastUniversalHashFunction0.numberOfInstance--;
//        return new UniversalHashFunction();
    }
}
