package on2020_05.on2020_05_19_Dynamic_Programming_Practice_DIV_1.BoxTower0;



import template.binary.Bits;
import template.primitve.generated.datastructure.IntegerDiscreteMap;
import template.primitve.generated.datastructure.IntegerList;
import template.utils.ArrayIndex;

import java.util.Arrays;

public class BoxTower {
    ArrayIndex ai;
    int[] dp;

    public int tallestTower(int[] x, int[] y, int[] z) {
        int n = x.length;
        IntegerList list = new IntegerList();

        Box[] boxes = new Box[n];
        for (int i = 0; i < n; i++) {
            boxes[i] = new Box();
            boxes[i].xyz[0] = x[i];
            boxes[i].xyz[1] = y[i];
            boxes[i].xyz[2] = z[i];

            for (int j = 0; j < 3; j++) {
                boxes[i].max[j] = boxes[i].max(j);
                boxes[i].min[j] = boxes[i].min(j);
            }
        }
        ai = new ArrayIndex(1 << n, n, 3);
        dp = new int[ai.totalSize()];
        int inf = (int) 1e9;
        Arrays.fill(dp, -inf);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 3; j++) {
                dp[ai.indexOf(1 << i, i, j)] = boxes[i].xyz[j];
            }
        }
        for (int i = 1; i < 1 << n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < 3; k++) {
                    if (dp[ai.indexOf(i, j, k)] < 0) {
                        continue;
                    }
                    for (int tj = 0; tj < n; tj++) {
                        if (Bits.bitAt(i, tj) == 1) {
                            continue;
                        }
                        for (int tk = 0; tk < 3; tk++) {
                            if (boxes[j].max[k] > boxes[tj].max[tk] ||
                                    boxes[j].min[k] > boxes[tj].min[tk]) {
                                continue;
                            }
                            dp[ai.indexOf(i | (1 << tj), tj, tk)] = Math.max(dp[ai.indexOf(i | (1 << tj), tj, tk)],
                                    dp[ai.indexOf(i, j, k)] + boxes[tj].xyz[tk]);
                        }
                    }
                }
            }
        }

        int ans = -inf;
        for(int k = 0; k < 1 << n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < 3; j++) {
                    ans = Math.max(ans, dp[ai.indexOf(k, i, j)]);
                }
            }
        }
        return ans;
    }
}

class Box {
    int[] xyz = new int[3];
    int[] max = new int[3];
    int[] min = new int[3];

    public int max(int i) {
        int max = 0;
        for (int j = 0; j < 3; j++) {
            if (i == j) {
                continue;
            }
            max = Math.max(max, xyz[j]);
        }
        return max;
    }

    public int min(int i) {
        int min = Integer.MAX_VALUE;
        for (int j = 0; j < 3; j++) {
            if (i == j) {
                continue;
            }
            min = Math.min(min, xyz[j]);
        }
        return min;
    }
}
