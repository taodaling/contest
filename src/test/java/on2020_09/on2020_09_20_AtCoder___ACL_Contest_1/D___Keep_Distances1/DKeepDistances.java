package on2020_09.on2020_09_20_AtCoder___ACL_Contest_1.D___Keep_Distances1;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayDeque;
import java.util.Deque;

public class DKeepDistances {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int[] x = new int[n];
        in.populate(x);

        int[][] pre = new int[20][n];
        int[][] post = new int[20][n];
        long[][] preSum = new long[20][n];
        long[][] postSum = new long[20][n];

        Deque<Integer> dq = new ArrayDeque<>(n);
        dq.clear();
        for (int i = 0; i < n; i++) {
            while (!dq.isEmpty() && x[i] - x[dq.peekFirst()] >= k) {
                post[0][dq.removeFirst()] = i;
            }
            dq.addLast(i);
        }
        while (!dq.isEmpty()) {
            post[0][dq.removeFirst()] = n;
        }
        for (int i = n - 1; i >= 0; i--) {
            while (!dq.isEmpty() && x[dq.peekFirst()] - x[i] >= k) {
                pre[0][dq.removeFirst()] = i;
            }
            dq.addLast(i);
        }
        while (!dq.isEmpty()) {
            pre[0][dq.removeFirst()] = -1;
        }
        for (int i = 0; i < n; i++) {
            if (pre[0][i] >= 0) {
                preSum[0][i] += pre[0][i];
            }
            if (post[0][i] < n) {
                postSum[0][i] += post[0][i];
            }
        }

        for (int i = 0; i + 1 < 20; i++) {
            for (int j = 0; j < n; j++) {
                pre[i + 1][j] = pre[i][j] == -1 ? -1 : pre[i][pre[i][j]];
                post[i + 1][j] = post[i][j] == n ? n : post[i][post[i][j]];
                preSum[i + 1][j] = pre[i][j] == -1 ? preSum[i][j] :
                        preSum[i][j] + preSum[i][pre[i][j]];
                postSum[i + 1][j] = post[i][j] == n ? postSum[i][j] :
                        postSum[i][j] + postSum[i][post[i][j]];
            }
        }


        int q = in.readInt();

        for (int i = 0; i < q; i++) {
            int l = in.readInt() - 1;
            int r = in.readInt() - 1;

            int lr = l;
            long sumLR = l;
            int size = 1;
            for (int j = 20 - 1; j >= 0; j--) {
                if (post[j][lr] <= r) {
                    size += 1 << j;
                    sumLR += postSum[j][lr];
                    lr = post[j][lr];
                    continue;
                }
            }

            int rl = r;
            long sumRL = r;
            for (int j = 20 - 1; j >= 0; j--) {
                if (pre[j][rl] >= l) {
                    sumRL += preSum[j][rl];
                    rl = pre[j][rl];
                    continue;
                }
            }

            long ans = sumRL - sumLR + size;
            out.println(ans);
        }

    }


}


