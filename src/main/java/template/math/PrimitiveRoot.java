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
        //factors = rho.findAllFactors(phi).keySet().stream().mapToInt(Integer::intValue).toArray();
        factors = Factorization.factorizeNumberPrime(phi).toArray();
    }

    public PrimitiveRoot(int x) {
        this(new Modular(x));
    }

    public int findMinPrimitiveRoot() {
        if (mod.getMod() == 2) {
            return 1;
        }
        return findMinPrimitiveRoot(2);
    }

    public int findMaxPrimitiveRoot() {
        if (mod.getMod() == 2) {
            return 1;
        }
        return findMaxPrimitiveRoot(mod.getMod() - 1);
    }

    private int findMaxPrimitiveRoot(int since) {
        for (int i = since; i >= 1; i--) {
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

    private int findMinPrimitiveRoot(int since) {
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
