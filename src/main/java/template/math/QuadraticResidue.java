package template.math;

import template.binary.Bits;

import java.util.Random;

public class QuadraticResidue {
    final int mod;
    Power power;
    Random random = new Random();


    public QuadraticResidue(int mod) {
        this.mod = mod;
        power = new Power(mod);
    }

    /**
     * <p>return \sqrt{n} or -1 if it doesn't exist</p>
     * <p>O(\log_2 p)</p>
     */
    public int square(int n) {
        n = DigitUtils.mod(n, mod);
        if (n == 0) {
            return 0;
        }
        int p = mod;
        if (p == 2) {
            return n;
        }

        if (power.pow(n, (p - 1) / 2) != 1) {
            return -1;
        }
        while (true) {
            long a = random.nextInt(p);
            int w = DigitUtils.mod(a * a - n, mod);
            if (power.pow(w, (p - 1) / 2) == 1) {
                continue;
            }


            int pow = (p + 1) / 2;
            int i = 31 - Integer.numberOfLeadingZeros(pow);
            long real = 1;
            long img = 0;
            for (; i >= 0; i--) {
                long nReal = (real * real + img * img % mod * w) % mod;
                long nImg = real * img * 2 % mod;
                real = nReal;
                img = nImg;
                if (Bits.get(pow, i) == 1) {
                    nReal = (real * a + img * w) % mod;
                    nImg = (img * a + real) % mod;
                    real = nReal;
                    img = nImg;
                }
            }

            return (int) real;
        }
    }
}
