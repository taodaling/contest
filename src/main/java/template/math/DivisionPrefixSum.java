package template.math;

import template.binary.Bits;
import template.primitve.generated.datastructure.LongBinaryFunction;
import template.primitve.generated.datastructure.LongHashMap;

public class DivisionPrefixSum {
    LongFactorization factorization;
    LongHashMap values;
    LongHashMap ps;
    LongBinaryFunction merger;
    long identity;
    long[] primes;
    /**
     * @param identity
     * @param merger
     */
    public DivisionPrefixSum(long identity, LongBinaryFunction merger) {
        values = new LongHashMap((int) 2e5, false);
        ps = new LongHashMap((int) 2e6, false);
        this.merger = merger;
        this.identity = identity;
    }

    public void init(LongFactorization factorization) {
        this.factorization = factorization;
        this.primes = factorization.primes;
        ps.clear();
        values.clear();
        dirty = false;
    }

    boolean dirty = false;

    public void add(long x, long mod) {
        assert factorization.g % x == 0;
        values.modify(x, mod);
        dirty = true;
    }

    private void build() {
        if (dirty) {
            ps.clear();
            dirty = false;
        }
    }

    /**
     * O(\min(\log_2g, 15)g) for pre-handle and O(1) for query
     *
     * @param x
     * @return
     */
    public long prefixSum(long x) {
        build();
        return get0(mask(0, x));
    }

    private long mask(long head, long body) {
        return (head << 60) | body;
    }

    private long get0(long x) {
        long ans = ps.getOrDefault(x, Long.MIN_VALUE);
        if (ans == Long.MIN_VALUE) {
            int head = (int) (x >>> 60);
            long body = x & ~Bits.tailLongMask(4);
            ans = identity;
            if (head >= primes.length) {
                ans = values.getOrDefault(body, identity);
            } else {
                if (body % primes[head] == 0) {
                    ans = merger.apply(ans, get0(mask(head, body / primes[head])));
                }
                ans = merger.apply(ans, get0(mask(head + 1, body)));
            }
            ps.put(x, ans);
        }
        return ans;
    }
}
