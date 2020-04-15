package on2020_04.on2020_04_15_Codeforces___Codeforces_Round__635__Div__1_.B__Xenia_and_Colorful_Gems;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongRange2DequeAdapter;
import template.rand.Randomized;

import java.util.Arrays;

public class BXeniaAndColorfulGems {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int na = in.readInt();
        int nb = in.readInt();
        int nc = in.readInt();
        long[] a = new long[na];
        long[] b = new long[nb];
        long[] c = new long[nc];
        for (int i = 0; i < na; i++) {
            a[i] = in.readInt();
        }
        for (int i = 0; i < nb; i++) {
            b[i] = in.readInt();
        }
        for (int i = 0; i < nc; i++) {
            c[i] = in.readInt();
        }

        long ans1 = solve2(a, b, c);
        out.println(ans1);
    }

    public long pow2(long a, long b) {
        long d = a - b;
        return d * d;
    }

    public long solve2(long[] a, long[] b, long[] c) {
        Randomized.shuffle(a);
        Randomized.shuffle(b);
        Randomized.shuffle(c);
        Arrays.sort(a);
        Arrays.sort(b);
        Arrays.sort(c);

        long ans = Long.MAX_VALUE;
        ans = Math.min(solve(a, b, c), ans);
        ans = Math.min(solve(a, c, b), ans);
        ans = Math.min(solve(b, a, c), ans);
        ans = Math.min(solve(b, c, a), ans);
        ans = Math.min(solve(c, a, b), ans);
        ans = Math.min(solve(c, b, a), ans);

        return ans;
    }

    public long solve(long[] a, long[] b, long[] c) {
        LongRange2DequeAdapter da = new LongRange2DequeAdapter(i -> a[i], 0, a.length - 1);
        LongRange2DequeAdapter db = new LongRange2DequeAdapter(i -> b[i], 0, b.length - 1);
        LongRange2DequeAdapter dc = new LongRange2DequeAdapter(i -> c[i], 0, c.length - 1);

        long ans = Long.MAX_VALUE;
        long av = da.removeFirst();
        long cv = dc.removeFirst();
        while (!db.isEmpty()) {
            long bv = db.removeFirst();
            while (bv > av && !da.isEmpty()) {
                av = da.removeFirst();
            }
            while (cv <= bv && !dc.isEmpty() && dc.peekFirst() <= bv) {
                cv = dc.removeFirst();
            }
            if (av >= bv && bv >= cv) {
                ans = Math.min(ans, pow2(av, bv) + pow2(av, cv) + pow2(bv, cv));
            }
        }
        return ans;
    }
}
