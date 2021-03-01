package template.math;

import java.util.HashMap;

public class LongPrimitiveRoot {
    private long[] factors;
    private long mod;
    private LongPower pow;
    private long phi;
    private static HashMap<Long, Long> root = new HashMap<>();
    private ILongModular modular;

    public static long findAnyRoot(long x) {
        if (!root.containsKey(x)) {
            root.put(x, new LongPrimitiveRoot(x).findMinPrimitiveRoot());
        }
        return root.get(x);
    }

    public LongPrimitiveRoot(long x) {
        phi = x - 1;
        mod = x;
        modular = ILongModular.getInstance(mod);
        pow = new LongPower(modular);
        factors = LongPollardRho.findAllFactors(phi).stream().mapToLong(Long::longValue).toArray();
    }

    public long findMinPrimitiveRoot() {
        if (mod == 2) {
            return 1;
        }
        return findMinPrimitiveRoot(2);
    }

    private long findMinPrimitiveRoot(long since) {
        for (long i = since; i < mod; i++) {
            boolean flag = true;
            for (long f : factors) {
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
