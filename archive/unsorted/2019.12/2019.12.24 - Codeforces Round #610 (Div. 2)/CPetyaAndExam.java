package contest;

import template.datastructure.Array2DequeAdapter;
import template.datastructure.SimplifiedDeque;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.IntegerArray2IntegerDequeAdapter;
import template.primitve.generated.IntegerDeque;
import template.primitve.generated.IntegerList;

import java.util.Arrays;

public class CPetyaAndExam {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int t = in.readInt();
        int a = in.readInt();
        int b = in.readInt();
        Problem[] ts = new Problem[n];
        int[] cnts = new int[2];
        for (int i = 0; i < n; i++) {
            ts[i] = new Problem();
            ts[i].type = in.readInt();
            cnts[ts[i].type]++;
        }

        IntegerList list = new IntegerList(n + 1);
        for (int i = 0; i < n; i++) {
            ts[i].limits = in.readInt();
            list.add(ts[i].limits);
        }
        list.add(t + 1);
        list.unique();


        Arrays.sort(ts, (x, y) -> x.limits - y.limits);
        SimplifiedDeque<Problem> deque = new Array2DequeAdapter<>(ts);

        long ans = 0;
        long[] diff = new long[2];
        for (int i = 0; i < list.size(); i++) {
            int until = list.get(i);
            while (!deque.isEmpty() && deque.peekFirst().limits < until) {
                diff[deque.removeFirst().type]++;
            }
            long solved = diff[0] + diff[1];
            long remain = until - 1;
            remain -= diff[0] * a + diff[1] * b;
            if (remain < 0) {
                continue;
            }
            long easy = Math.min(remain / a, cnts[0] - diff[0]);
            solved += easy;
            remain -= a * easy;
            long hard = Math.min(remain / b, cnts[1] - diff[1]);
            solved += hard;
            ans = Math.max(ans, solved);
        }

        out.println(ans);
    }
}

class Problem {
    int type;
    int limits;
}
