package template.rand;

public class FastUniversalHashFunction4 implements HashFunction {

    static final FastUniversalHashFunction4 INSTANCE = new FastUniversalHashFunction4();

    private FastUniversalHashFunction4() {
    }

    @Override
    public long f(long x) {
        long ans = x * 2654435761L;
        return ans ^ (ans >>> 32);
    }

    @Override
    public HashFunction upgrade() {
//        return FastUniversalHashFunction5.INSTANCE;
        FastUniversalHashFunction0.numberOfInstance--;
        return new UniversalHashFunction();
    }
}
