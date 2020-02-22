package template.math;

import template.binary.Bits;

import java.util.Map;
import java.util.Random;

public class QuadraticResidue {
    final Modular modular;
    Power power;
    final PollardRho rho = new PollardRho();
    Random random = new Random();


    public QuadraticResidue(Modular modular) {
        this.modular = modular;
        power = new Power(modular);
    }

    /**
     * return \sqrt{n} or -1 if it doesn't exist
     */
    public int square(int n) {
        n = modular.valueOf(n);
        if (n == 0) {
            return 0;
        }
        int p = modular.m;
        if (power.pow(n, (p - 1) / 2) != 1) {
            return -1;
        }
        while (true) {
            int a = random.nextInt(p);
            int w = modular.plus(modular.mul(a, a), -n);
            if (power.pow(w, (p - 1) / 2) == 1) {
                continue;
            }


            int pow = (p + 1) / 2;
            int i = 31 - Integer.numberOfLeadingZeros(pow);
            int real = 1;
            int img = 0;
            for (; i >= 0; i--) {
                int nReal = modular.plus(modular.mul(real, real), modular.mul(modular.mul(img, img), w));
                int nImg = modular.mul(modular.mul(real, img), 2);
                real = nReal;
                img = nImg;
                if (Bits.bitAt(pow, i) == 1) {
                    nReal = modular.plus(modular.mul(real, a), modular.mul(img, w));
                    nImg = modular.plus(modular.mul(img, a), real);
                    real = nReal;
                    img = nImg;
                }
            }

            return real;
        }
    }

    public int minPrimitiveRoot() {
        if (modular.m == 2) {
            return 1;
        }
        Map<Integer, Integer> factorMap = rho.findAllFactors(modular.m - 1);
        int[] factors = factorMap.keySet().stream().mapToInt(Integer::intValue).toArray();
        for (int i = 2;; i++) {
            boolean valid = true;
            for (int factor : factors) {
                if (power.pow(i, (modular.m - 1) / factor) == 1) {
                    valid = false;
                    break;
                }
            }
            if (valid) {
                return i;
            }
        }
    }
}
