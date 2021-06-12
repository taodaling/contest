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

    public static int rankOf(long[] p) {
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

    public static double[] normalizeAndReplace(double[] p) {
        int r = rankOf(p);
        return PrimitiveBuffers.resize(p, r + 1);
    }

    public static void moduleInPlace(int[] a, int[] b) {
        int ra = Polynomials.rankOf(a);
        int rb = Polynomials.rankOf(b);
        if (ra < rb) {
            return;
        }
        for (int i = ra; i >= rb; i--) {
            assert a[i] % b[rb] == 0;
            int d = a[i] / b[rb];
            if(d == 0){
                continue;
            }
            for (int j = rb; j >= 0; j--) {
                a[i + j - rb] -= d * b[j];
            }
        }
    }

    public static int[][] divAndRemainder(int[] a, int[] b) {
        int ra = Polynomials.rankOf(a);
        int rb = Polynomials.rankOf(b);
        if (ra < rb) {
            return new int[][]{PrimitiveBuffers.allocIntPow2(1), PrimitiveBuffers.allocIntPow2(a)};
        }
        int[] div = PrimitiveBuffers.allocIntPow2(ra - rb + 1);
        int[] op = PrimitiveBuffers.allocIntPow2(a);

        for (int i = ra; i >= rb; i--) {
            assert op[i] % b[rb] == 0;
            int d = op[i] / b[rb];
            div[i - rb] = d;
            if(d == 0){
                continue;
            }
            for (int j = rb; j >= 0; j--) {
                op[i + j - rb] -= d * b[j];
            }
        }

        op = Polynomials.normalizeAndReplace(op);
        return new int[][]{div, op};
    }

    public static int[] mul(int[] a, int[] b) {
        int ra = Polynomials.rankOf(a);
        int rb = Polynomials.rankOf(b);
        int[] ans = PrimitiveBuffers.allocIntPow2(ra + rb + 1);
        for (int i = 0; i <= ra; i++) {
            for (int j = 0; j <= rb; j++) {
                ans[i + j] += a[i] * b[j];
            }
        }
        return ans;
    }
}
