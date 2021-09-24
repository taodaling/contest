package on2021_09.on2021_09_19_AtCoder___AtCoder_Regular_Contest_126.B___Cross_free_Matching;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerGenericBIT;

import java.util.Arrays;
import java.util.Comparator;

public class BCrossFreeMatching {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        Pair[] ps = new Pair[m];
        for (int i = 0; i < m; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            ps[i] = new Pair(a, b);
        }
        Arrays.sort(ps, Comparator.<Pair>comparingInt(x -> x.b).thenComparingInt(x -> -x.a));
        IntegerGenericBIT bit = new IntegerGenericBIT(n, Math::max, 0);
        for (Pair p : ps) {
            int best = bit.query(p.a) + 1;
            bit.update(p.a + 1, best);
        }
        int ans = bit.query(n);
        out.println(ans);
    }
}

class Pair {
    int a;
    int b;

    public Pair(int a, int b) {
        this.a = a;
        this.b = b;
    }
}