package on2019_10.on2019_10_12_Atcoder_AGC005.TaskB;



import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Deque;

import template.FastInput;

public class TaskB {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int[] perm = new int[n + 1];
        int[] prev = new int[n + 1];
        int[] next = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            perm[i] = in.readInt();
        }
        Deque<Integer> decQueue = new ArrayDeque<>(n);
        Deque<Integer> incQueue = new ArrayDeque<>(n);
        for (int i = 1; i <= n; i++) {
            while (decQueue.size() > 0 && perm[decQueue.peekLast()] > perm[i]) {
                decQueue.removeLast();
            }
            if (decQueue.size() == 0) {
                prev[i] = 1;
            } else {
                prev[i] = decQueue.peekLast() + 1;
            }
            decQueue.addLast(i);
        }
        for (int i = n; i >= 1; i--) {
            while (incQueue.size() > 0 && perm[incQueue.peekFirst()] >= perm[i]) {
                incQueue.removeFirst();
            }
            if (incQueue.size() == 0) {
                next[i] = n;
            } else {
                next[i] = incQueue.peekFirst() - 1;
            }
            incQueue.addFirst(i);
        }

        long ans = 0;
        for(int i = 1; i <= n; i++){
            int l = i - prev[i] + 1;
            int r = next[i] + 1 - i;
            ans += (long)l * r * perm[i];
        }

        out.println(ans);
    }
}
