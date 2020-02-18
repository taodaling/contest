package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Bits;
import template.primitve.generated.IntegerList;
import template.primitve.generated.IntegerMultiWayDeque;
import template.primitve.generated.IntegerMultiWayStack;

import java.util.Arrays;

public class DSwapAndFlip {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        int[] b = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        for (int i = 0; i < n; i++) {
            b[i] = in.readInt();
        }
        int[] vals = new int[n];
        IntegerMultiWayDeque deque = new IntegerMultiWayDeque(51, n);
        int[] masks = new int[n];
        IntegerList perm = new IntegerList(n);
        int minCnt = (int) 1e8;

        for (int i = 0; i < (1 << n); i++) {
            boolean valid = true;
            for (int j = 0; j < n; j++) {
                masks[j] = Bits.bitAt(i, j);
                if (masks[j] == 1) {
                    vals[j] = b[j];
                } else {
                    vals[j] = a[j];
                }
            }
            for (int j = 0; j < n; j++) {
                deque.addLast(vals[j], j);
            }
            int cnt = 0;
            for (int j = 0; j < n; j++) {
                int p = 0;
                for (int t = 0; t < n; t++) {
                    if (vals[t] > vals[j] && t < j ||
                            vals[t] < vals[j] && t > j) {
                        p++;
                    }
                }
                cnt += p;
                masks[j] ^= p & 1;
            }
            cnt /= 2;

            for (int j = 1; j <= 50; j++) {
                perm.clear();
                while (!deque.isEmpty(j)) {
                    int last = deque.removeFirst(j);
                    perm.add(masks[last] ^ (perm.size() & 1));
                }
                if (perm.isEmpty()) {
                    continue;
                }
                int sum = 0;
                int m = perm.size();
                for (int k = 0; k < m; k++) {
                    sum += perm.get(k);
                }
                if (sum != perm.size() / 2) {
                    valid = false;
                }
                sum = 0;
                for (int k = 0; k < m; k++) {
                    int val = perm.get(k);
                    if (val == 0) {
                        continue;
                    }
                    sum += val;
                    int target = sum * 2 - 1;
                    int dist = Math.abs(target - k);
                    cnt += dist;
                }
            }

            if (!valid) {
                continue;
            }
            minCnt = Math.min(minCnt, cnt);
        }

        if (minCnt == (int) 1e8) {
            out.println(-1);
        } else {
            out.println(minCnt);
        }
    }
}
