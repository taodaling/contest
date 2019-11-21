package template.math;

import java.util.HashMap;
import java.util.Map;

/**
 * Extend lucas algorithm
 */
public class ExtLucas {
    PollardRho pr = new PollardRho();
    Map<Integer, ExtLucasFactorial> factorialMap = new HashMap();

    public ExtLucas(int p) {
        Map<Integer, Integer> factors = pr.findAllFactors(p);
        for (Map.Entry<Integer, Integer> entry : factors.entrySet()) {
            factorialMap.put(entry.getValue(),
                            new ExtLucasFactorial(entry.getKey(), entry.getValue(), new int[entry.getValue() + 1]));
        }
    }

    /**
     * Get C(m, n) % p
     */
    public int composite(long m, long n) {
        ExtCRT extCRT = new ExtCRT();
        for (Map.Entry<Integer, ExtLucasFactorial> entry : factorialMap.entrySet()) {
            extCRT.add(entry.getValue().composite(m, n), entry.getKey());
        }
        return (int) extCRT.r;
    }

    private static class ExtLucasFactorial {
        int exp;
        int fact;
        int p;
        int pc;
        Modular modular;
        Power power;
        ExtGCD extGCD = new ExtGCD();
        int[] g;

        /**
         * O(pc)
         *
         * @param p the prime
         * @param pc p^c
         * @param g buffer
         */
        public ExtLucasFactorial(int p, int pc, int[] g) {
            this.p = p;
            this.pc = pc;
            this.g = g;
            modular = new Modular(pc);
            power = new Power(modular);
            g[0] = 1;
            g[1] = 1;
            for (int i = 2; i <= pc; i++) {
                if (i % p == 0) {
                    g[i] = g[i - 1];
                } else {
                    g[i] = modular.mul(g[i - 1], i);
                }
            }
        }

        /**
         * return m! (mod pc) without any factor which is multiple of p. <br>
         * O(\log_2^2{m})
         */
        private int fact(long m) {
            fact = 1;
            exp = 0;
            while (m > 1) {
                exp += m / p;
                fact = modular.mul(fact, power.pow(g[pc], m / pc));
                fact = modular.mul(fact, g[(int) (m % pc)]);
                m /= p;
            }
            return fact;
        }

        /**
         * Find C(m,n), it means choose n elements from a set whose size is m. <br>
         * O(\log_2^2{m})
         */
        public int composite(long m, long n) {
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
