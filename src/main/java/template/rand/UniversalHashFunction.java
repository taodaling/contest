package template.rand;

import template.math.ILongModular;
import template.math.LongModular2305843009213693951;

public class  UniversalHashFunction implements HashFunction {
    public static int numberOfInstance = 0;

    private static ILongModular modular = LongModular2305843009213693951.getInstance();
    private static long mod = modular.getMod();
    long a;
    long b;



    {
        numberOfInstance++;
        upgrade();
    }

    public long f(long x) {
        long ans = modular.mul(x, a);
        ans += b;
        if (ans >= mod) {
            ans -= mod;
        }
        return ans;
    }

    @Override
    public HashFunction upgrade() {
        this.a = RandomWrapper.INSTANCE.nextLong(1, mod - 1);
        this.b = RandomWrapper.INSTANCE.nextLong(0, mod - 1);
        return this;
    }
}
