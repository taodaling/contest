package template.polynomial;

import template.math.DigitUtils;
import template.math.ILongModular;
import template.math.Power;
import template.utils.PrimitiveBuffers;

public class IntPolyMTT extends IntPolyFFT {
    static int[] mods = new int[]{469762049, 998244353, 167772161};


    static IntPolyNTT[] ntts = new IntPolyNTT[]{
            new IntPolyNTT(mods[0]),
            new IntPolyNTT(mods[1]),
            new IntPolyNTT(mods[2])
    };

    static ILongModular mod2 = ILongModular.getInstance((long) mods[0] * mods[1]);
    static Power[] powers = new Power[]{
            new Power(mods[0]),
            new Power(mods[1]),
            new Power(mods[2])
    };
    static int inv10 = powers[0].inverse(mods[1]);
    static int inv01 = powers[1].inverse(mods[0]);
    static int inv012 = powers[2].inverse((int) ((long) mods[0] * mods[1] % mods[2]));
    static long p1inv10 = (long) mods[1] * inv10;
    static long p0inv01 = (long) mods[0] * inv01;

    public IntPolyMTT(int mod) {
        super(mod);
    }


    public static void main(String[] args) {
        new IntPolyMTT((int)1e9 + 7).convolution(new int[]{10000000}, new int[]{10000000});
    }

    @Override
    public int[] convolution(int[] a, int[] b) {
        if (Math.min(a.length, b.length) < (int) 2e5) {
            return super.convolution(a, b);
        }
        int[] c0 = ntts[0].convolution(a, b);
        int[] c1 = ntts[1].convolution(a, b);
        int[] c2 = ntts[2].convolution(a, b);
        int maxlen = Math.max(c0.length, c1.length);
        maxlen = Math.max(maxlen, c2.length);
        int[] ans = PrimitiveBuffers.allocIntPow2(maxlen);
        for (int i = 0; i < maxlen; i++) {
            int a0 = i < c0.length ? c0[i] : 0;
            int a1 = i < c1.length ? c1[i] : 0;
            int a2 = i < c2.length ? c2[i] : 0;
            long t0 = mod2.plus(mod2.mul(a0, p1inv10),
                    mod2.mul(a1, p0inv01));
            long t1 = DigitUtils.mod(a2 - t0, mods[2]) * (long) inv012 % mods[2];
            ans[i] = DigitUtils.mod(t1 * mods[0] % mod * mods[1] + t0, mod);
        }
        PrimitiveBuffers.release(c0, c1, c2);
        return ans;
    }
}
