package contest;

import template.binary.Bits;
import template.binary.Log2;
import template.geometry.geo2.IntegerPoint2;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IfritBomber {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int r = in.readInt();

        IntegerPoint2[] pts = new IntegerPoint2[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new IntegerPoint2(in.readInt(), in.readInt());
        }

        int[] mask = new int[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (IntegerPoint2.dist2(pts[i], pts[j]) <= r * r) {
                    mask[i] |= 1 << j;
                }
            }
        }

        IntegerList choose = new IntegerList(n);
        int ans = 0;
        for (int i = 0; i < n; i++) {
            if (Integer.bitCount(mask[i]) == 1) {
                ans |= 1 << i;
            } else {
                choose.add(i);
            }
        }

        int[] chooseMask = new int[choose.size()];
        for (int i = 0; i < choose.size(); i++) {
            chooseMask[i] = mask[choose.get(i)];
        }

        depth = new int[n];
        edge = mask;
        visited = new boolean[n];
        for (int i = 0; i < n; i++) {
            dfs(i, 0);
        }

        int[] sep = new int[2];
        for (int i = 0; i < choose.size(); i++) {
            int x = choose.get(i);
            sep[depth[x] % 2] |= 1 << i;
        }

        int greedy = Integer.bitCount(sep[0]) < Integer.bitCount(sep[1]) ? sep[0] : sep[1];

        int maskPick = solve(chooseMask, greedy);
        for (int i = 0; i < choose.size(); i++) {
            if (Bits.get(maskPick, i) == 1) {
                ans |= 1 << choose.get(i);
            }
        }

        out.println(Integer.bitCount(ans));
        for (int i = 0; i < n; i++) {
            if (Bits.get(ans, i) == 1) {
                out.append(i + 1).append(' ');
            }
        }


    }

    public int comb(int n, int m) {
        return m == 0 ? 1 : (comb(n - 1, m - 1) * n / m);
    }

    int[] depth;
    int[] edge;
    boolean[] visited;

    public void dfs(int root, int d) {
        if (visited[root]) {
            return;
        }
        visited[root] = true;
        depth[root] = d;
        for (int i = 0; i < 30; i++) {
            if (Bits.get(edge[root], i) == 1) {
                dfs(i, d + 1);
            }
        }
    }

    public int solve(int[] masks, int greedy) {
        if (masks.length == 0) {
            return 0;
        }

        int sum = 0;
        for (int x : masks) {
            sum |= x;
        }
        int leftCnt = masks.length / 2;
        int rightCnt = masks.length - leftCnt;

        int[][] leftIndex = new int[leftCnt + 1][];
        int[] leftSize = new int[leftCnt + 1];
        for (int i = 0; i <= leftCnt; i++) {
            leftIndex[i] = new int[comb(leftCnt, i)];
        }
        int[] leftMasks = new int[1 << leftCnt];
        for (int i = 0; i < 1 << leftCnt; i++) {
            int bitCount = Integer.bitCount(i);
            leftIndex[bitCount][leftSize[bitCount]++] = i;
            if (i > 0) {
                int log = Log2.floorLog(i);
                leftMasks[i] = leftMasks[i - (1 << log)] | masks[log];
            }
        }

        int[] rightMasks = new int[1 << rightCnt];
        for (int i = 0; i < 1 << rightCnt; i++) {
            if (i > 0) {
                int log = Log2.floorLog(i);
                rightMasks[i] = rightMasks[i - (1 << log)] | masks[log + leftCnt];
            }
        }

        int ans = greedy;
        for (int i = 0; i < leftMasks.length; i++) {
            if (leftMasks[i] == sum && Integer.bitCount(ans) > Integer.bitCount(i)) {
                ans = i;
            }
        }

        for (int i = 0; i < 1 << rightCnt; i++) {
            int cur = Integer.bitCount(ans) - Integer.bitCount(i) - 1;
            cur = Math.min(cur, leftCnt);
            while (cur >= 0) {
                boolean find = false;
                for (int index : leftIndex[cur]) {
                    if ((rightMasks[i] | leftMasks[index]) == sum) {
                        ans = (i << leftCnt) | index;
                        find = true;
                        cur--;
                        break;
                    }
                }
                if (!find) {
                    break;
                }
            }
        }


        return ans;
    }
}
