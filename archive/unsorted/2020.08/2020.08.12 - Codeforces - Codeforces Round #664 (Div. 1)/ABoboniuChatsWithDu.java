package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongPreSum;
import template.rand.Randomized;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class ABoboniuChatsWithDu {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int d = in.readInt();
        int m = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        int[] greater = Arrays.stream(a).filter(x -> x > m).toArray();
        int[] less = Arrays.stream(a).filter(x -> x <= m).toArray();
        Randomized.shuffle(greater);
        Randomized.shuffle(less);
        Arrays.sort(greater);
        Arrays.sort(less);
        SequenceUtils.reverse(greater);
        SequenceUtils.reverse(less);
        LongPreSum lps = new LongPreSum(i -> less[i], less.length);
        long ans = lps.intervalSum(0, n - 1);
        long sum = 0;
        for (int i = 0; i < greater.length && (d + 1) * i + 1 <= n; i++) {
            sum += greater[i];
            int remain = n - (d + 1) * i - 1;
            long cand = sum + lps.intervalSum(0, remain - 1);
            ans = Math.max(ans, cand);
        }

        out.println(ans);
    }
}
