package template.polynomial;

import template.utils.PrimitiveBuffers;

public class Polynomials {
    public static int rankOf(int[] p) {
        int r = p.length - 1;
        while (r >= 0 && p[r] == 0) {
            r--;
        }
        return Math.max(0, r);
    }

    public static int rankOf(double[] p) {
        int r = p.length - 1;
        while (r >= 0 && -1e-12 < p[r] && p[r] < 1e-12) {
            r--;
        }
        return Math.max(0, r);
    }

    public static int[] normalizeAndReplace(int[] p) {
        int r = rankOf(p);
        return PrimitiveBuffers.resize(p, r + 1);
    }

    public double[] normalizeAndReplace(double[] p) {
        int r = rankOf(p);
        return PrimitiveBuffers.resize(p, r + 1);
    }
}
