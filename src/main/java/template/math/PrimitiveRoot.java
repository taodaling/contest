package template.math;

import java.util.HashMap;

public class PrimitiveRoot {
    private int[] factors;
    private int mod;
    private Power pow;
    private int phi;
    private static HashMap<Integer, Integer> root = new HashMap<>();

    public static int findAnyRoot(int x) {
        if (!root.containsKey(x)) {
            root.put(x, new PrimitiveRoot(x).findMinPrimitiveRoot());
        }
        return root.get(x);
    }

    public PrimitiveRoot(int x) {
        phi = x - 1;
        mod = x;
        pow = new Power(mod);
        factors = Factorization.factorizeNumberPrime(phi).toArray();
    }

    public int findMinPrimitiveRoot() {
        if (mod == 2) {
            return 1;
        }
        return findMinPrimitiveRoot(2);
    }

    public int findMaxPrimitiveRoot() {
        if (mod == 2) {
            return 1;
        }
        return findMaxPrimitiveRoot(mod - 1);
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
        for (int i = since; i < mod; i++) {
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
