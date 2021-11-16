package template.rand;

public class FastUniversalHashFunction5 implements HashFunction {

    static final FastUniversalHashFunction5 INSTANCE = new FastUniversalHashFunction5();

    private FastUniversalHashFunction5() {
    }

    long xorshift(long n, int i) {
        return n ^ (n >>> i);
    }

    @Override
    public long f(long x) {
        long p = 0x5555555555555555L; // pattern of alternating 0 and 1
        long c = 7316035218449499591L;// random uneven integer constant;
        return c * xorshift(p * xorshift(x, 32), 32);
    }

    @Override
    public HashFunction upgrade() {
        return FastUniversalHashFunction1.INSTANCE;
        //        FastUniversalHashFunction0.numberOfInstance--;
//        return new UniversalHashFunction();
    }
}
