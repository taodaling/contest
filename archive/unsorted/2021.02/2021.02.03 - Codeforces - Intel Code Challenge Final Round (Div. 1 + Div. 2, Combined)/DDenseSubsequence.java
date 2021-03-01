package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerComparator;
import template.primitve.generated.datastructure.IntegerMinQueue;

import java.util.Arrays;

public class DDenseSubsequence {
    int[] dp;
    int inf = (int) 1e9;
    IntegerMinQueue queue;

    //1 for choice, 2 for necessary, 0 for forbidden
    public int solve(int[] s, int m) {
        int n = s.length;
        queue.clear();
        for (int i = 0; i < m; i++) {
            queue.addLast(0);
        }
        for (int i = 0; i < n; i++) {
            if (s[i] > 0) {
                dp[i] = queue.min() + 1;
            } else {
                dp[i] = inf;
            }
            if (s[i] == 2) {
                queue.clear();
            }
            queue.addLast(dp[i]);
            if (queue.size() > m) {
                queue.removeFirst();
            }
        }
        return queue.min();
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int m = in.ri();
        char[] s = new char[(int) 1e5];
        int n = in.rs(s);
        s = Arrays.copyOf(s, n);
        int[] cast = new int[n];
        dp = new int[n];
        queue = new IntegerMinQueue(n, IntegerComparator.NATURE_ORDER);
        for (int i = 'a'; i <= 'z'; i++) {
            for (int j = 0; j < n; j++) {
                cast[j] = s[j] == i ? 1 : s[j] < i ? 2 : 0;
            }
            int res = solve(cast, m);
            if (res >= inf) {
                continue;
            }
            IntegerArrayList seq = new IntegerArrayList(res);
            for (int j = 0; j < n; j++) {
                if (s[j] < i) {
                    seq.add(s[j]);
                }
            }
            while (seq.size() < res) {
                seq.add(i);
            }
            seq.sort();
            for (int x : seq.toArray()) {
                out.append((char) x);
            }
            return;
        }
    }
}
