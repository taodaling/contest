package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerComparator;
import template.primitve.generated.datastructure.IntegerMinQueue;
import template.utils.Debug;

import java.util.Arrays;

public class BookShopII {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int x = in.readInt();
        int[] weight = new int[n];
        int[] value = new int[n];
        int[] cnt = new int[n];
        in.populate(weight);
        in.populate(value);
        in.populate(cnt);
        int[] prev = new int[x + 1];
        int[] next = new int[x + 1];
        IntegerMinQueue mq = new IntegerMinQueue(n, IntegerComparator.REVERSE_ORDER);
        for (int i = 0; i < n; i++) {
            Arrays.fill(next, 0);
            int w = weight[i];
            int v = value[i];
            int c = cnt[i];
            for (int j = 0; j < w; j++) {
                mq.clear();
                for (int t = 0; j + t * w <= x; t++) {
                    mq.addLast(prev[j + t * w] - t * v);
                    if (mq.size() > c + 1) {
                        mq.removeFirst();
                    }
                    next[j + t * w] = t * v + mq.min();
                }
            }
            int[] tmp = prev;
            prev = next;
            next = tmp;
        }

        int ans = Arrays.stream(prev).max().orElse(-1);
        out.println(ans);
    }
}
