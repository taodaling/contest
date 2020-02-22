package template.math;

import template.binary.Bits;
import template.binary.CachedLog2;
import template.datastructure.Loop;
import template.polynomial.FastFourierTransform;
import template.polynomial.Polynomials;
import template.primitve.generated.datastructure.IntegerList;

/**
 * 计算C(n,0),C(n,1),...,C(n,m)在模p的情况下的值，其中p不一定是素数。
 * 时间复杂度是暴力计算：O(m^2 log_2n)或使用FFT：O(m log_2m log_2n)
 */
public class BinomialComposite {
    private int m;
    private Modular mod;
    private IntegerList composites;
    private boolean fft;

    public BinomialComposite(long n, int m, Modular mod, boolean fft) {
        this.m = m;
        this.mod = mod;
        this.fft = fft;
        composites = pow(n);
        composites.expandWith(0, m + 1);
    }

    /**
     * Return C(n, i)
     */
    public int get(int i) {
        return composites.get(i);
    }

    /**
     * return a * b
     */
    private void mul(IntegerList a, IntegerList b, IntegerList c) {
        if (!fft) {
            Polynomials.mul(a, b, c, mod);
        } else {
            int[] ans = FastFourierTransform.multiplyMod(a.getData(), a.size(), b.getData(), b.size(), mod.getMod());
            c.clear();
            c.addAll(ans);
        }
        trim(c);
    }

    private void trim(IntegerList x) {
        if (x.size() > m + 1) {
            x.remove(m + 1, x.size() - 1);
        }
    }

    /**
     * return p * (x + 1)
     */
    private void mul(IntegerList p, IntegerList ans) {
        ans.clear();
        ans.expandWith(0, p.size() + 1);

        int n = p.size();
        for (int i = 0; i < n; i++) {
            int val = p.get(i);
            ans.set(i, mod.plus(ans.get(i), val));
            ans.set(i + 1, val);
        }

        trim(ans);
    }

    private IntegerList pow(long exp) {
        int ceil = CachedLog2.ceilLog(m + 1 + m);
        Loop<IntegerList> loop = new Loop<>(new IntegerList(1 << ceil), new IntegerList(1 << ceil));
        loop.get().add(1);
        for (int i = CachedLog2.floorLog(exp); i >= 0; i--) {
            mul(loop.get(), loop.get(), loop.turn());
            if (1 == Bits.bitAt(exp, i)) {
                mul(loop.get(), loop.turn());
            }
        }
        return loop.get();
    }
}
