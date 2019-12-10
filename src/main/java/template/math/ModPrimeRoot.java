package template.math;

import template.datastructure.IntHashMap;
import template.datastructure.IntHashSet;
import template.datastructure.IntIterator;
import template.datastructure.IntList;

public class ModPrimeRoot {
    private RelativePrimeModLog log;
    private Modular mod;
    private Modular powMod;
    private static ExtGCD extGCD = new ExtGCD();
    private static Gcd gcd = new Gcd();
    private Power power;
    private int primitiveRoot;

    public ModPrimeRoot(Modular p) {
        this(p, new PrimitiveRoot(p).findMinPrimitiveRoot());
    }

    public ModPrimeRoot(Modular p, int g) {
        mod = p;
        log = new RelativePrimeModLog(g, mod);
        powMod = mod.getModularForPowerComputation();
        power = new Power(mod);
        primitiveRoot = g;
    }

    public void allRoot(int x, int k, IntList list) {
        x = mod.valueOf(x);
        k = powMod.valueOf(k);
        if (x == 0) {
            if (k == 0) {
                return;
            }
            list.add(0);
            return;
        }
        if (k == 0) {
            if (x == 1) {
                for (int i = 0, end = mod.getMod(); i < end; i++) {
                    list.add(i);
                }
                return;
            }
            return;
        }

        int logx = log.log(x);
        int gcd = (int) extGCD.extgcd(k, powMod.getMod());
        if (logx % gcd != 0) {
            return;
        }
        int loga = powMod.mul(logx / gcd, powMod.valueOf(extGCD.getX()));
        int phi = powMod.getMod();
        phi = phi / ModPrimeRoot.gcd.gcd(phi, k);
        loga %= phi;
        int first = power.pow(primitiveRoot, loga);
        int step = power.pow(primitiveRoot, phi);
        IntHashSet set = new IntHashSet(1, true);
        for (; !set.contain(first); first = mod.mul(first, step)) {
            set.add(first);
            list.add(first);
        }
    }

    /**
     * Find x^{1/k}, if there doesn't exist such number, -1 will be returned
     */
    public int root(int x, int k) {
        x = mod.valueOf(x);
        k = powMod.valueOf(k);
        if (x == 0) {
            if (k == 0) {
                return -1;
            }
            return 0;
        }
        if (k == 0) {
            if (x == 1) {
                return 1;
            }
            return -1;
        }

        int logx = log.log(x);
        int gcd = (int) extGCD.extgcd(k, powMod.getMod());
        if (logx % gcd != 0) {
            return -1;
        }
        int loga = powMod.mul(logx / gcd, powMod.valueOf(extGCD.getX()));
        return power.pow(primitiveRoot, loga);
    }
}
