package template.rand;

public class FastUniversalHashFunction3 implements HashFunction {
    static final FastUniversalHashFunction3 INSTANCE = new FastUniversalHashFunction3();
    private FastUniversalHashFunction3(){}

    @Override
    public long f(long h) {
        h ^= h >>> 33;
        h *= 0xff51afd7ed558ccdL;
        h ^= h >>> 33;
        h *= 0xc4ceb9fe1a85ec53L;
        h ^= h >>> 33;
        return h;
    }

    @Override
    public HashFunction upgrade() {
//        return FastUniversalHashFunction4.INSTANCE;
        FastUniversalHashFunction0.numberOfInstance--;
        return new UniversalHashFunction();
    }
}
