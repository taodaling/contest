package template.math;

import template.math.Modular;
import template.math.PollardRho;
import template.math.Power;

public class PrimitiveRoot {
    private int[] factors;
    private Modular mod;
    private Power pow;
    int phi;
    private static PollardRho rho = new PollardRho();

    public PrimitiveRoot(int x) {
        phi = x - 1;
        mod = new Modular(x);
        pow = new Power(mod);
        factors = rho.findAllFactors(phi).keySet().stream().mapToInt(Integer::intValue).toArray();
    }

    public int findMinPrimitiveRoot() {
        return findMinPrimitiveRoot(2);
    }

    public int findMinPrimitiveRoot(int since) {
        for (int i = since; i < mod.m; i++) {
            boolean flag = true;
            for (int f : factors) {
                if (pow.pow(i, phi / f) == 1) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                return i;
            }
        }
        return -1;
    }
}
