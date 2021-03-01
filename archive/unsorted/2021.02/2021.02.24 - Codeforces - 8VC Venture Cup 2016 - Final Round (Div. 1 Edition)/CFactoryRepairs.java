package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongBIT;

public class CFactoryRepairs {
    int n;
    int a;
    int b;
    LongBIT mina;
    LongBIT minb;
    long[] cnt;

    public void update(int d, int x) {
        mina.update(d, Math.min(cnt[d] + x, b) - Math.min(cnt[d], b));
        minb.update(d, Math.min(cnt[d] + x, a) - Math.min(cnt[d], a));
        cnt[d] += x;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        int k = in.ri();
        a = in.ri();
        b = in.ri();
        int q = in.ri();
        cnt = new long[n + 1];
        mina = new LongBIT(n);
        minb = new LongBIT(n);

        for (int i = 0; i < q; i++) {
            int t = in.ri();
            if (t == 1) {
                int d = in.ri();
                int a = in.ri();
                update(d, a);
            } else {
                int p = in.ri();
                int l = p;
                int r = l + k - 1;
                long sum = mina.query(l - 1) + minb.query(r + 1, n);
                out.println(sum);
            }
        }
    }
}
