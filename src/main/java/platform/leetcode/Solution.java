package platform.leetcode;


import java.util.Comparator;
import java.util.TreeSet;

public class Solution {
    public int largestSubmatrix(int[][] mat) {
        TreeSet<int[]> set = new TreeSet<>((x, y) -> x[0] == y[0] ? Integer.compare(x[1], y[1]) :
                Integer.compare(x[0], y[0]));
        int idIndicator = 0;
        int ans = 0;
        int n = mat.length;
        int m = mat[0].length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (mat[i][j] == 1 && (i == 0 || mat[i - 1][j] == 0)) {
                    int to = i;
                    while (to + 1 < n && mat[to + 1][j] == 1) {
                        to++;
                    }
                    set.add(new int[]{to, idIndicator++});
                }
            }
            while (!set.isEmpty() && set.first()[0] < i) {
                set.pollFirst();
            }
            int remain = set.size();
            for (int[] x : set) {
                int area = (x[0] - i + 1) * remain;
                ans = Math.max(ans, area);
                remain--;
            }
        }

        return ans;
    }
}

