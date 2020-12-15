package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Permutation;
import template.math.Power;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.List;

public class BMoodularArithmetic {
    int mod = (int) 1e9 + 7;
    Power pow = new Power(mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int p = in.ri();
        int k = in.ri();
        if (k == 0) {
            out.println(pow.pow(p, p - 1));
            return;
        }
        int[] to = new int[p];
        for (int i = 0; i < p; i++) {
            to[i] = (int) ((long) i * k % p);
        }
        Permutation perm = new Permutation(to);
        List<IntegerArrayList> circles = perm.extractCircles();
        long[] ways = new long[p];
        for (IntegerArrayList list : circles) {
            ways[list.size()] += list.size();
        }
        for (int i = p - 1; i >= 1; i--) {
            for (int j = i + i; j < p; j += i) {
                ways[j] += ways[i];
            }
        }
        for (int i = 0; i < p; i++) {
            ways[i] %= mod;
        }
        long ans = 1;
        for (IntegerArrayList list : circles) {
            ans = ans * ways[list.size()] % mod;
        }
        out.println(ans);
    }
}
