package template.math;

public class PrimitiveRoot {
    private int[] factors;
    private Modular mod;
    private Power pow;
    int phi;
    private static PollardRho rho = new PollardRho();

    public PrimitiveRoot(Modular x) {
        phi = x.getMod() - 1;
        mod = x;
        pow = new Power(mod);
        factors = rho.findAllFactors(phi).keySet().stream().mapToInt(Integer::intValue).toArray();
    }

    public PrimitiveRoot(int x) {
        this(new Modular(x));
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
