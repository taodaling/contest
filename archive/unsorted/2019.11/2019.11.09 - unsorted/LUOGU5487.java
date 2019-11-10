package contest;

import template.*;

public class LUOGU5487 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        NumberTheory.Modular mod = new NumberTheory.Modular(998244353);
        NumberTheory.Power pow = new NumberTheory.Power(mod);
        NumberTheoryTransform ntt = new NumberTheoryTransform(mod);

        int n = in.readInt();
        int m = in.readInt();
        int[] seq = new int[n];
        for (int i = 0; i < n; i++) {
            seq[i] = mod.valueOf(in.readInt());
        }

        ModLinearFeedbackShiftRegister bm = new ModLinearFeedbackShiftRegister(mod, n);
        for (int i = 0; i < n; i++) {
            bm.add(seq[i]);
        }

        int length = bm.length();
        DigitUtils.Log2 log2 = new DigitUtils.Log2();
        int proper = log2.ceilLog(length + 1) + 2;
        int[] p = new int[1 << proper];
        int[] remainder = new int[1 << proper];

        p[length] = 1;
        for (int i = 1; i <= length; i++) {
            p[length - i] = mod.valueOf(-bm.codeAt(i));
        }

        for(int i = 1; i <= length; i++){
            out.append(bm.codeAt(i)).append(' ');
        }
        out.println();

        ntt.module(m, p, remainder, proper);
        int rank = ntt.rankOf(remainder, proper);

        int pm = 0;
        for(int i = 0; i <= rank; i++){
            pm = mod.plus(pm, mod.mul(remainder[i], seq[i]));
        }
        out.println(pm);
    }
}
