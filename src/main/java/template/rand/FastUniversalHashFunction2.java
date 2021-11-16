package template.rand;

public class FastUniversalHashFunction2 implements HashFunction {
    static final FastUniversalHashFunction2 INSTANCE = new FastUniversalHashFunction2();

    private FastUniversalHashFunction2() {
    }

    @Override
    public long f(long x) {
        x = (x ^ (x >>> 30)) * 0xbf58476d1ce4e5b9L;
        x = (x ^ (x >>> 27)) * 0x94d049bb133111ebL;
        x = x ^ (x >>> 31);
        return x;
    }

    @Override
    public HashFunction upgrade() {
        return FastUniversalHashFunction3.INSTANCE;
//        FastUniversalHashFunction0.numberOfInstance--;
//        return new UniversalHashFunction();
    }
}
