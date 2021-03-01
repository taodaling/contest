package template.math;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Extend lucas algorithm
 */
public class ExtLucas implements LargeIntCombination, Iterable<Map.Entry<Integer, LargeIntCombination>> {
    PollardRho pr = new PollardRho();
    Map<Integer, LargeIntCombination> factorialMap = new HashMap();
    int p;

    @Override
    public Iterator<Map.Entry<Integer, LargeIntCombination>> iterator() {
        return factorialMap.entrySet().iterator();
    }

    /**
     * <pre>
     * Time complexity:O(\sum_i \min(m, p_i^c_i)
     * Space complexity:O(\sum_i \min(m, p_i^c_i)
     * </pre>
     *
     * @param p modular
     * @param m limitation
     */
    public ExtLucas(int p, long m) {
        this.p = p;
        Set<Integer> factors = pr.findAllFactors(p);
        for (int factor : factors) {
            int exp = 1;
            while (p / exp % factor == 0) {
                exp *= factor;
            }
            LargeIntCombination combination;
            if (p / factor % factor != 0) {
                combination = new Lucas(new Combination((int) Math.min(m, factor - 1), factor),
                        factor);
            } else {
                combination = new ExtLucasFactorial(factor, exp, m);
            }
            factorialMap.put(exp, combination);
        }
    }

    /**
     * Get C(m, n) % p
     * <br>
     * O(\sum_i \log_{p_i} m \log_2 m)
     */
    public int combination(long m, long n) {
        if (m < n) {
            return 0;
        }
        IntExtCRT extCRT = new IntExtCRT();
        for (Map.Entry<Integer, LargeIntCombination> entry : factorialMap.entrySet()) {
            extCRT.add(entry.getValue().combination(m, n), entry.getKey());
        }

//        if (extCRT.r != BigCombination.combination(m, n).mod(BigInteger.valueOf(p)).intValue()) {
//            throw new RuntimeException();
//        }
        return extCRT.getValue();
    }

    public static class ExtLucasFactorial implements LargeIntCombination {
        int exp;
        int fact;
        int p;
        int pc;
        int mod;
        Power power;
        int[] g;

        /**
         * O(min(pc, m))
         *
         * @param p  the prime
         * @param pc p^c
         */
        public ExtLucasFactorial(int p, int pc, long m) {
            this.p = p;
            this.pc = pc;
            this.g = new int[(int) (Math.min(pc, m) + 1)];
            mod = pc;
            power = new Power(pc);
            g[0] = 1;
            g[1] = 1;
            for (int i = 2; i < g.length; i++) {
                if (i % p == 0) {
                    g[i] = g[i - 1];
                } else {
                    g[i] = (int) (((long) g[i - 1] * i) % mod);
                }
            }
        }

        /**
         * return m! (mod pc) without any factor which is multiple of p. <br>
         * O(\log_p m \log_2 m)
         */
        private int fact(long m) {
            fact = 1;
            exp = 0;
            while (m > 1) {
                exp += m / p;
                if (m >= pc) {
                    //There can be optimize to O(1), so this method provide O(\log_p m) time complexity.
                    fact = (int) ((long) fact * power.pow(g[pc], m / pc) % mod);
                }
                fact = (int) ((long) fact * g[(int) (m % pc)] % mod);
                m /= p;
            }
            return fact;
        }


        /**
         * Find C(m,n), it means choose n elements from a set whose size is m. <br>
         * O(\log_p m \log_2 m)
         */
        public int combination(long m, long n) {
            if (m < n) {
                return 0;
            }
            long v = fact(m);
            int e = exp;
            v = v * power.inverseExtGCD(fact(n)) % mod;
            e -= exp;
            v = v * power.inverse(fact(m - n)) % mod;
            e -= exp;
            v = v * power.pow(p, e) % mod;
            return DigitUtils.mod(v, mod);
        }
    }
}
