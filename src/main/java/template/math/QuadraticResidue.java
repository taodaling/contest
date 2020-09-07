package template.math;

import template.binary.Bits;

import java.util.Random;

public class QuadraticResidue {
    final Modular modular;
    Power power;
    Random random = new Random();


    public QuadraticResidue(Modular modular) {
        this.modular = modular;
        power = new Power(modular);
    }

    /**
     * <p>return \sqrt{n} or -1 if it doesn't exist</p>
     * <p>O(\log_2 p)</p>
     */
    public int square(int n) {
        n = modular.valueOf(n);
        if (n == 0) {
            return 0;
        }
        int p = modular.getMod();
        if (p == 2) {
            return n;
        }

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
                if (Bits.get(pow, i) == 1) {
                    nReal = modular.plus(modular.mul(real, a), modular.mul(img, w));
                    nImg = modular.plus(modular.mul(img, a), real);
                    real = nReal;
                    img = nImg;
                }
            }

            return real;
        }
    }
}
