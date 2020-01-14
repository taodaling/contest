package on2020_01.on2020_01_14_Educational_Codeforces_Round_80__Rated_for_Div__2_.D__Minimax_Problem;



import template.algo.IntBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Bits;

import java.util.Arrays;

public class DMinimaxProblem {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[][] mat = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = in.readInt();
            }
        }

        IntBinarySearch ibs = new IntBinarySearch() {
            @Override
            public boolean check(int mid) {
                return find(mid, mat, n, m) == null;
            }
        };

        int mid = ibs.binarySearch(0, (int)1e9 + 1) - 1;
        int[] ans = find(mid, mat, n, m);
        out.printf("%d %d", ans[0] + 1, ans[1] + 1);
    }

    public int[] find(int mid, int[][] mat, int n, int m){
        int[] masks = new int[1 << m];
        Arrays.fill(masks, -1);
        for (int i = 0; i < n; i++) {
            int mask = 0;
            for (int j = 0; j < m; j++) {
                mask = Bits.setBit(mask, j, mat[i][j] >= mid);
            }
            masks[mask] = i;
        }
        int target = masks.length - 1;
        for (int i = 0; i < masks.length; i++) {
            for (int j = 0; j < masks.length; j++) {
                if (masks[i] != -1 && masks[j] != -1 && (i | j) == target) {
                    return new int[]{masks[i], masks[j]};
                }
            }
        }
        return null;
    }

}
