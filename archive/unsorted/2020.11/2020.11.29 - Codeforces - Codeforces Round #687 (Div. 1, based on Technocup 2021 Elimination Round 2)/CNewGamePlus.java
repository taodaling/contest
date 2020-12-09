package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;
import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Random;

public class CNewGamePlus {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int block = k + 1;
        long[] c = new long[n];
        in.populate(c);
        Randomized.shuffle(c);
        Arrays.sort(c);
        SequenceUtils.reverse(c);
        PriorityQueue<Long> top = new PriorityQueue<>(n, (a, b) -> b.compareTo(a));
        for (int i = 0; i < block; i++) {
            top.add(0L);
        }
        long ans = 0;
        for (long x : c) {
            long head = top.remove();
            ans += head;
            head += x;
            top.add(head);
        }

        out.println(ans);
    }
}
