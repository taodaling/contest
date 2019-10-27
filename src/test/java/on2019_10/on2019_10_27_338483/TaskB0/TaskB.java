package on2019_10.on2019_10_27_338483.TaskB0;




import template.FastInput;
import template.FastOutput;

import java.util.*;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[][] lrs = new int[n][2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2; j++) {
                lrs[i][j] = in.readInt();
            }
        }
        Arrays.sort(lrs, (a, b) -> a[0] - b[0]);
        Deque<int[]> que = new ArrayDeque<>(Arrays.asList(lrs));
        List<int[]> remain = new ArrayList<>(n);
        while (!que.isEmpty() && que.peekFirst()[0] == 0) {
            remain.add(que.removeFirst());
        }
        PriorityQueue<int[]> leftSide = new PriorityQueue<>(n, (a, b) -> a[1] - b[1]);
        for (int i = 1; i <= m; i++) {
            if (que.isEmpty()) {
                break;
            }
            leftSide.add(que.removeFirst());
            while (!que.isEmpty() && que.peekFirst()[0] == i) {
                if (leftSide.peek()[1] < que.peekFirst()[1]) {
                    remain.add(leftSide.remove());
                    leftSide.add(que.removeFirst());
                } else {
                    remain.add(que.removeFirst());
                }
            }
        }

        int cnt = leftSide.size();
        remain.sort((a, b) -> -(a[1] - b[1]));
        int next = m;
        for (int[] lr : remain) {
            if (lr[1] > next) {
                continue;
            }
            next--;
            cnt++;
        }

        cnt = Math.min(cnt, m);
        out.println(n - cnt);
    }
}
