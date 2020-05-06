package template.math;

import template.binary.Log2;
import template.utils.SequenceUtils;

public class Nimber {
    private static int[][] nimProductCache = new int[16][16];
    private static int[][] nimProductPowerCache = new int[16][16];

    static {
        SequenceUtils.deepFill(nimProductCache, -1);
        SequenceUtils.deepFill(nimProductPowerCache, -1);
    }

    public static long product(long a, long b) {
        if (a < b) {
            return product(b, a);
        }
        if (a < 16 && nimProductCache[(int) a][(int) b] != -1) {
            return nimProductCache[(int) a][(int) b];
        }
        long ans;
        if (b <= 1) {
            ans = a * b;
        } else {
            long m = getLargestFermatNumber(a);
            long p = a / m;
            long q = a % m;
            long s = b / m;
            long t = b % m;

            long c1 = product(p, s);
            long c2 = product(p, t) ^ product(q, s);
            long c3 = product(q, t);
            ans = ((c1 ^ c2) * m) ^ c3 ^ productPower(m / 2, c1);
        }

        if (a < 16) {
            nimProductCache[(int) a][(int) b] = (int) ans;
        }

        return ans;
    }

    private static long productPower(long a, long b) {
        if (a < 16 && nimProductPowerCache[(int) a][(int) b] != -1) {
            return nimProductPowerCache[(int) a][(int) b];
        }
        long ans;
        if (b <= 1 || a <= 1) {
            ans = a * b;
        } else {
            long m = getLargestFermatNumber(a);
            long p = a / m;
            long s = b / m;
            long t = b % m;

            long d1 = productPower(p, s);
            long d2 = productPower(p, t);
            ans = ((d1 ^ d2) * m) ^ productPower(m / 2, d1);
        }

        if (a < 16) {
            nimProductPowerCache[(int) a][(int) b] = (int) ans;
        }

        return ans;
    }

    /**
     * 2 ^ {2 ^ y} <= x
     */
    private static long getLargestFermatNumber(long x) {
        int y = Log2.floorLog(Log2.floorLog(x));
        return 1L << (1 << y);
    }
}
