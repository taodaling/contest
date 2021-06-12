package on2021_05.on2021_05_23_AtCoder___AtCoder_Regular_Contest_120.D___Bracket_Score_2;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SortUtils;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.IntStream;

public class DBracketScore2 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri() * 2;
        int[] a = in.ri(n);
        int[] index = IntStream.range(0, n).toArray();
        SortUtils.quickSort(index, (i, j) -> Integer.compare(a[i], a[j]), 0, n);
        int[] value = new int[n];
        for (int i = 0; i < n / 2; i++) {
            value[index[i]] = -1;
        }
        for (int i = n / 2; i < n; i++) {
            value[index[i]] = 1;
        }
        char[] seq = new char[n];
        Deque<Integer> dq = new ArrayDeque<>(n);
        int cur = 0;
        for (int i = 0; i < n; i++) {
            if (dq.isEmpty()) {
                cur = value[i];
                dq.addLast(i);
            } else if (cur == value[i]) {
                dq.addLast(i);
            } else {
                seq[dq.removeLast()] = '(';
                seq[i] = ')';
            }
        }
        for (char c : seq) {
            out.append(c);
        }

    }
}
