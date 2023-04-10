package platform.leetcode;


import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.TreeSet;

public class Solution {
    public static void main(String[] args) {
        new Solution().minimumVisitedCells(new int[][]{{3, 4, 2, 1}, {4, 2, 3, 1}, {2, 1, 0, 0}, {2, 4, 0, 0}});
    }

    public int minimumVisitedCells(int[][] grid) {
        int n = grid.length;
        int m = grid[0].length;
        TreeSet<Integer>[] rows = new TreeSet[n];
        TreeSet<Integer>[] cols = new TreeSet[m];
        for (int i = 0; i < n; i++) {
            rows[i] = new TreeSet<>();
            for (int j = 0; j < m; j++) {
                rows[i].add(j);
            }
        }
        for (int i = 0; i < m; i++) {
            cols[i] = new TreeSet<>();
            for (int j = 0; j < n; j++) {
                cols[i].add(j);
            }
        }
        int inf = (int) 1e8;
        int[][] dist = new int[n][m];
        for (int[] row : dist) {
            Arrays.fill(row, inf);
        }
        dist[0][0] = 0;
        Deque<int[]> dq = new ArrayDeque<>();
        dq.addLast(new int[]{0, 0});
        rows[0].remove(0);
        cols[0].remove(0);
        boolean debug = true;
        while (!dq.isEmpty()) {
            int[] head = dq.removeFirst();
            if (debug) {
                System.out.println(Arrays.toString(head));
            }
            int x = head[0];
            int y = head[1];
            int r = grid[x][y] + y;
            int b = grid[x][y] + x;

            while (true) {
                var next = rows[x].higher(y);
                if (next == null || next > r) {
                    break;
                }
                dist[x][next] = dist[x][y] + 1;
                rows[x].remove(next);
                cols[next].remove(x);
                dq.addLast(new int[]{x, next});
            }
            while (true) {
                var next = cols[y].higher(x);
                if (next == null || next > b) {
                    break;
                }
                dist[next][y] = dist[x][y] + 1;
                rows[next].remove(y);
                cols[y].remove(next);
                dq.addLast(new int[]{next, y});
            }
        }

        int ans = dist[n - 1][m - 1];
        if (ans == inf) {
            return -1;
        }
        return ans;
    }

}

class Event {
    int l;
    int r;
    int x;
    int y;
    int dist;
}