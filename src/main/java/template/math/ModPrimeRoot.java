package template.math;

import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerHashSet;

public class ModPrimeRoot {
    private CoprimeModLog log;
    private int mod;
    private int powMod;
    private static IntExtGCDObject extGCD = new IntExtGCDObject();
    private Power power;
    private int primitiveRoot;

    public ModPrimeRoot(int p) {
        this(p, PrimitiveRoot.findAnyRoot(p));
    }

    public ModPrimeRoot(int p, int g) {
        mod = p;
        log = new CoprimeModLog(g, mod);
        powMod = mod - 1;
        power = new Power(mod);
        primitiveRoot = g;
    }

    public void allRoot(int x, int k, IntegerArrayList list) {
        x = DigitUtils.mod(x, mod);
        k = DigitUtils.mod(k, powMod);
        if (x == 0) {
            list.add(0);
            return;
        }
        if (k == 0) {
            if (x == 1) {
                for (int i = 1, end = mod; i < end; i++) {
                    list.add(i);
                }
                return;
            }
            return;
        }

        int logx = log.log(x);
        int gcd = extGCD.extgcd(k, powMod);
        if (logx % gcd != 0) {
            return;
        }
        int loga = DigitUtils.mod((long) logx / gcd * extGCD.getX(), powMod);
        int phi = powMod;
        phi = phi / GCDs.gcd(phi, k);
        loga %= phi;
        int first = power.pow(primitiveRoot, loga);
        int step = power.pow(primitiveRoot, phi);
        IntegerHashSet set = new IntegerHashSet(1, true);
        for (; !set.contain(first); first = (int) ((long) first * step % mod)) {
            set.add(first);
            list.add(first);
        }
    }

    /**
     * <p>Find x^{1/k}, if there doesn't exist such number, -1 will be returned</p>
     * <p>O(p^0.5)</p>
     */
    public int root(int x, int k) {
        x = DigitUtils.mod(x, mod);
        if (x == 0) {
            if (k == 0) {
                return -1;
            }
            return 0;
        }
        k = DigitUtils.mod(k, powMod);
        if (k == 0) {
            if (x == 1) {
                return 1;
            }
            return -1;
        }

        int logx = log.log(x);
        int gcd = extGCD.extgcd(k, powMod);
        if (logx % gcd != 0) {
            return -1;
        }
        int loga = DigitUtils.mod((long) logx / gcd * extGCD.getX(), powMod);
        return power.pow(primitiveRoot, loga);
    }
}
