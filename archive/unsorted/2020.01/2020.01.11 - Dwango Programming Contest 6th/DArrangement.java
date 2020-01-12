package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.IntegerList;

import java.util.PriorityQueue;

public class DArrangement {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] next = new int[n];
        for (int i = 0; i < n; i++) {
            next[i] = in.readInt() - 1;
        }

        if (n == 2) {
            out.println(-1);
            return;
        }

        PriorityQueue<Integer> queue = new PriorityQueue<>(n);
        IntegerList ans = new IntegerList(n);
        int[] x = null;
        if(n == 3){
            x = bf(-1, next, queue);
        }
        int j = -1;
        while (x == null && j < n) {
            j++;
            ans.clear();
            ans.add(j);
            for (int i = 0; i < n; i++) {
                if(i != j)
                    queue.add(i);
            }
            int forbid = next[j];
            while (queue.size() > 3) {
                if (queue.peek() != forbid) {
                    ans.add(queue.remove());
                } else {
                    queue.remove();
                    ans.add(queue.remove());
                    queue.add(forbid);
                }
                forbid = next[ans.tail()];
            }
            x = bf(forbid, next, queue);
        }
        if(x == null){
            out.println(-1);
            return;
        }
        ans.addAll(x);
        for (int i = 0; i < n; i++) {
            out.append(ans.get(i) + 1).append(' ');
        }
    }

    public int[] bf(int forbid, int[] next, PriorityQueue<Integer> pq) {
        int a = pq.remove();
        int b = pq.remove();
        int c = pq.remove();
        int[][] allWay = new int[][]{
                {a, b, c},
                {a, c, b},
                {b, a, c},
                {b, c, a},
                {c, a, b},
                {c, b, a}
        };

        for (int[] way : allWay) {
            if (forbid != way[0] && next[way[0]] != way[1] && next[way[1]] != way[2]) {
                return way;
            }
        }
        return null;
    }
}
