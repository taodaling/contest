package template.math;

import template.rand.RandomWrapper;

import java.util.*;

public class LongPollardRho {
    public static void main(String[] args) {
        System.out.println(LongPollardRho.findAllFactors(1323668450567L));
    }
    private LongPollardRho() {

    }

    static long[] smallPrimes = new long[]{2, 3, 5, 7, 11, 13, 17, 19};

    public static long findFactor(long n) {
        if (n == 1) {
            return 1;
        }
        if (LongMillerRabin.mr(n, 10)) {
            return n;
        }
        while (true) {
            long f = rho(n);
            if (f != n) {
                return f;
            }
        }
    }

    public static Set<Long> findAllFactors(long n) {
        if (n == 0) {
            throw new IllegalArgumentException();
        }

        Set<Long> set = new HashSet<>();
        for (long p : smallPrimes) {
            if (n % p != 0) {
                continue;
            }
            set.add(p);
            while (n % p == 0) {
                n /= p;
            }
        }
        findAllFactors(set, n);
        return set;
    }

    private static void findAllFactors(Set<Long> set, long n) {
        if (n == 1) {
            return;
        }
        long f = findFactor(n);
        if (f == n) {
            set.add(f);
            return;
        }
        long otherPart = n / f;
//        long g = GCDs.gcd(f, otherPart);
//        while (g != 1) {
//            otherPart /= g;
//            g = GCDs.gcd(f, otherPart);
//        }
        findAllFactors(set, f);
        findAllFactors(set, otherPart);
    }

    private static long rho(long n) {
        if (n % 2 == 0) {
            return 2;
        }
        if (n % 3 == 0) {
            return 3;
        }
        ILongModular modular = ILongModular.getInstance(n);
        long x = 0, y = x, t, q = 1, c = RandomWrapper.INSTANCE.nextLong(n - 1);
        for (int k = 2; ; k <<= 1, y = x, q = 1) {
            for (int i = 1; i <= k; ++i) {
                x = modular.plus(modular.mul(x, x), c);
                q = modular.mul(q, Math.abs(x - y));
                if ((i & 127) == 0) {
                    t = GCDs.gcd(q, n);
                    if (t > 1) {
                        return t;
                    }
                }
            }
            if ((t = GCDs.gcd(q, n)) > 1) {
                return t;
            }
        }
    }
}
