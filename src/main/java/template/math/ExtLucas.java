package template.math;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Extend lucas algorithm
 */
public class ExtLucas implements LongCombination, Iterable<Map.Entry<Integer, LongCombination>> {
    PollardRho pr = new PollardRho();
    Map<Integer, LongCombination> factorialMap = new HashMap();
    int p;

    @Override
    public Iterator<Map.Entry<Integer, LongCombination>> iterator() {
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
        Map<Integer, Integer> factors = pr.findAllFactors(p);
        for (Map.Entry<Integer, Integer> entry : factors.entrySet()) {
            LongCombination combination;
            if (entry.getKey().equals(entry.getValue())) {
                combination = new Lucas(new Combination((int) Math.min(m, entry.getKey()), new Modular(entry.getKey())),
                        entry.getKey());
            } else {
                combination = new ExtLucasFactorial(entry.getKey(), entry.getValue(), m);
            }
            factorialMap.put(entry.getValue(), combination);
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
        for (Map.Entry<Integer, LongCombination> entry : factorialMap.entrySet()) {
            extCRT.add(entry.getValue().combination(m, n), entry.getKey());
        }

//        if (extCRT.r != BigCombination.combination(m, n).mod(BigInteger.valueOf(p)).intValue()) {
//            throw new RuntimeException();
//        }
        return extCRT.getValue();
    }

    public static class ExtLucasFactorial implements LongCombination {
        int exp;
        int fact;
        int p;
        int pc;
        Modular modular;
        Power power;
        IntExtGCDObject extGCD = new IntExtGCDObject();
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
            modular = new Modular(pc);
            power = new Power(modular);
            g[0] = 1;
            g[1] = 1;
            for (int i = 2; i < g.length; i++) {
                if (i % p == 0) {
                    g[i] = g[i - 1];
                } else {
                    g[i] = modular.mul(g[i - 1], i);
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
                    fact = modular.mul(fact, power.pow(g[pc], m / pc));
                }
                fact = modular.mul(fact, g[(int) (m % pc)]);
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
            int v = fact(m);
            int e = exp;
            extGCD.extgcd(fact(n), pc);
            v = modular.mul(v, modular.valueOf(extGCD.getX()));
            e -= exp;
            extGCD.extgcd(fact(m - n), pc);
            v = modular.mul(v, modular.valueOf(extGCD.getX()));
            e -= exp;
            v = modular.mul(v, power.pow(p, e));
            return v;
        }
    }
}
