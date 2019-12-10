package contest;

import geometry.Complex;
import template.datastructure.IntList;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.polynomial.NumberTheoryTransform;
import template.polynomial.Polynomials;

public class FLunarNewYearAndARecursiveSequence {
    Modular mod = new Modular(998244353);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int k = in.readInt();
        int[] bs = new int[k];
        for (int i = 0; i < k; i++) {
            bs[i] = in.readInt();
        }
        int n = in.readInt();
        int m = in.readInt();
        IntList polynomial = new IntList(k + 1);
        for (int i = k - 1; i >= 0; i--) {
            polynomial.add(mod.valueOf(-bs[i]));
        }
        polynomial.add(1);

        IntList remainder = new IntList(k);
        Polynomials.module(n, remainder, );
    }
}


