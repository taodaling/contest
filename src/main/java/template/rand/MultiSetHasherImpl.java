package template.rand;

import template.math.LongModular2305843009213693951;

public class MultiSetHasherImpl implements MultiSetHasher {
    static LongModular2305843009213693951 mod = LongModular2305843009213693951.getInstance();

    @Override
    public long hash(long x) {
        return RandomWrapper.INSTANCE.nextLong(1, mod.getMod() - 1);
    }

    @Override
    public long merge(long a, long b) {
        return mod.plus(a, b);
    }

    @Override
    public long remove(long a, long b) {
        return mod.subtract(a, b);
    }

    @Override
    public long mul(long set, long time) {
        return mod.mul(set, time);
    }
}
