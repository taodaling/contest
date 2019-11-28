package contest;



import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int m = in.readInt();
        int n = in.readInt();
        int k = in.readInt();
        int t = in.readInt();
        int[] a = new int[m];
        for (int i = 0; i < m; i++) {
            a[i] = in.readInt();
        }
        int[][] traps = new int[k][3];
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < 3; j++) {
                traps[i][j] = in.readInt();
            }
        }
        Randomized.randomizedArray(a, 0, m);
        Arrays.sort(a);
        Arrays.sort(traps, (x, y) -> x[0] - y[0]);

        int l = 0;
        int r = m;
        while (l < r) {
            int mid = (l + r + 1) >>> 1;
            if (check(a, traps, n, mid) <= t) {
                l = mid;
            } else {
                r = mid - 1;
            }
        }

        out.println(l);
    }

    Deque<int[]> deque = new ArrayDeque<>(200000);

    public int check(int[] a, int[][] traps, int n, int mid) {
        int agi;
        if (mid > 0) {
            agi = a[a.length - mid];
        } else {
            agi = (int) 1e9;
        }
        int users = 0;
        deque.clear();
        deque.addAll(Arrays.asList(traps));
        int time = 0;
        while (users < n + 1) {
            int initPos = users;
            while (!deque.isEmpty() && deque.peekFirst()[0] <= users + 1) {
                int[] trap = deque.removeFirst();
                if (trap[2] <= agi) {
                    continue;
                }
                if (trap[1] > users) {
                    time += trap[1] - users;
                    users = trap[1];
                }
            }
            if (users > initPos) {
                time += users - initPos;
                users = initPos;
            } else {
                time++;
                users++;
            }
        }

        return time;
    }
}
