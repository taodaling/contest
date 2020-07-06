package contest;

import template.datastructure.Range2DequeAdapter;
import template.datastructure.SimplifiedDeque;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;
import template.primitve.generated.datastructure.IntegerRange2DequeAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class DKThK {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        PriorityQueue<Cond> pq = new PriorityQueue<>(n, (a, b) -> Integer.compare(a.index, b.index));

        int[] xs = new int[n];
        int[] ans = new int[n * n];
        Arrays.fill(ans, -1);
        for (int i = 0; i < n; i++) {
            int x = in.readInt() - 1;
            xs[i] = x;
            Cond cond = new Cond();
            cond.remain = i;
            cond.index = x;
            cond.e = i;
            ans[cond.index] = cond.e;
            if (cond.remain > 0) {
                pq.add(cond);
            }
        }

        for (int i = 0; i < n * n; i++) {
            if (!pq.isEmpty() && pq.peek().index <= i) {
                out.println("No");
                return;
            }

            if (ans[i] != -1) {
                Cond cond = new Cond();
                cond.index = n * n;
                cond.remain = n - ans[i] - 1;
                cond.e = ans[i];
                if(cond.remain > 0) {
                    pq.add(cond);
                }
                continue;
            }


            if (pq.isEmpty()) {
                out.println("No");
                return;
            }
            Cond head = pq.remove();
            head.remain--;
            ans[i] = head.e;
            if (head.remain > 0) {
                pq.add(head);
            }


        }

        out.println("Yes");
        for (int x : ans) {
            out.append(x + 1).append(' ');
        }
    }
}

class Cond {
    int remain;
    int index;
    int e;
}