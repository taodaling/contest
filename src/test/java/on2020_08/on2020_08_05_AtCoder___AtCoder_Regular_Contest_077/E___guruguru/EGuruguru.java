package on2020_08.on2020_08_05_AtCoder___AtCoder_Regular_Contest_077.E___guruguru;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerBIT;
import template.primitve.generated.datastructure.LongBIT;
import template.utils.Debug;

public class EGuruguru {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] target = new int[n];
        in.populate(target);

        LongBIT aBit = new LongBIT(m * 2 + 1);
        LongBIT bBit = new LongBIT(m * 2 + 1);

        long sum = 0;
        for (int i = 1; i < n; i++) {
            int from = target[i - 1];
            int to = target[i];
            if (to < from) {
                to += m;
            }
            sum += to - from;
            update(aBit, from + 1, to, 1);
            update(bBit, from + 1, to, -(from + 1));
        }

        int cur = 0;
        long maxReduce = 0;
        for (int i = 1; i <= m; i++) {
            long a1 = aBit.query(i);
            long a2 = aBit.query(i + m);
            long b = bBit.query(i) + bBit.query(i + m);

//            if(sum - estimate(target, i, m) != (a1 * i + a2 * (m + i) + b)){
//                throw new RuntimeException();
//            }

            maxReduce = Math.max(maxReduce, a1 * i + a2 * (m + i) + b);
        }

        out.println(sum - maxReduce);
    }

    public long estimate(int[] seq, int prefer, int m) {
        long ans = 0;
        for (int i = 1; i < seq.length; i++) {
            int from = seq[i - 1];
            int to = seq[i];
            if (to < from) {
                to += m;
            }
            long min = to - from;
            int usedPrefer = prefer;
            if (usedPrefer < from) {
                usedPrefer += m;
            }
            if (usedPrefer <= to) {
                min = Math.min(min, 1 + to - usedPrefer);
            }
            ans += min;
        }
        return ans;
    }

    public void update(LongBIT bit, int l, int r, long val) {
        bit.update(l, val);
        bit.update(r + 1, -val);
    }
}
