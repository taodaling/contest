package template.math;

import template.primitve.generated.datastructure.LongHashMap;

/**
 * <p>
 * https://www.zhihu.com/question/29580448/answer/45218281
 * </p>
 */
public class PrimeCounter {
    private static int lim = (int) 1e6;
    private static EulerSieve es = new EulerSieve(lim);
    private Function f;
    private long[] primeSum;

    public static interface Function {
        long f(long p);

        /**
         * return \sum_{i=2}^n f(i)
         *
         * @return
         */
        long sum(long n);
    }

    public PrimeCounter(Function f) {
        this.f = f;

        primeSum = new long[lim + 1];
        for (int i = 1; i <= lim; i++) {
            if (es.isPrime(i)) {
                primeSum[i] = f.f(i);
            }
            primeSum[i] += primeSum[i - 1];
        }
    }

    LongHashMap map = new LongHashMap((int) 2e7, true);

    private long calc(long v, int p) {
        if (p <= 1) {
            return f.sum(v);
        }
        if ((long) p * p > v) {
            return calc(v, IntMath.floorSqrt(v));
        }
        p = es.prevPrime(p);
        long key = v * lim + p;
        long ans = map.getOrDefault(key, -1);
        if (ans == -1) {
            ans = calc(v, p - 1) - f.f(p) * (calc(v / p, p - 1) - primeSum[p - 1]);
            map.put(key, ans);
        }
        return ans;
    }

    /**
     * get \sum_{i=2}^n [i is prime] f(i) in O(n^0.75)
     */
    public long get(long n) {
        long ans = calc(n, IntMath.floorSqrt(n));
        return ans;
    }
}
