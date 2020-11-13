package on2020_11.on2020_11_11_AtCoder___AtCoder_Beginner_Contest_149.E___Handshake;



import template.algo.LongBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongPreSum;
import template.problem.KthSmallestCardGroup;
import template.rand.Randomized;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class EHandshake {
    long ans = 0;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long m = in.readLong();
        long[] a = new long[n];
        in.populate(a);
        Randomized.shuffle(a);
        Arrays.sort(a);

        LongBinarySearch lbs = new LongBinarySearch() {
            @Override
            public boolean check(long mid) {
                long geq = countLarger(a, mid);
                return !(geq >= m);
            }
        };

        long pt = lbs.binarySearch(0, (int) 1e6, true);
        LongPreSum lps = new LongPreSum(i -> a[i], n);
        long remain = m;
        long sum = 0;
        for (int i = 0, r = a.length; i < a.length; i++) {
            while (r - 1 >= 0 && a[r - 1] + a[i] > pt) {
                r--;
            }
            int cnt = a.length - r;
            remain -= cnt;
            sum += a[i] * cnt + lps.post(r);
        }
        sum += remain * pt;
        out.println(sum);
    }

    public long countLarger(long[] a, long x) {
        long ans = 0;
        for (int i = 0, r = a.length; i < a.length; i++) {
            while (r - 1 >= 0 && a[r - 1] + a[i] >= x) {
                r--;
            }
            ans += a.length - r;
        }
        return ans;
    }
}
