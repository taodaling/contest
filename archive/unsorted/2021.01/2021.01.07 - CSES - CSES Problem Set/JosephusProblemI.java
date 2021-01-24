package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.problem.JosephusCircle;

import java.util.ArrayDeque;
import java.util.Deque;

public class JosephusProblemI {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Deque<Integer> dq = new ArrayDeque<>(n);
        for (int i = 1; i <= n; i++) {
            dq.addLast(i);
        }
        while (!dq.isEmpty()) {
            dq.addLast(dq.removeFirst());
            out.println(dq.removeFirst());
        }
    }
}

