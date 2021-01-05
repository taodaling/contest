package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeSet;

public class ATheTwoRoutes {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        boolean[][] railway = new boolean[n][n];
        for (int i = 0; i < m; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            railway[a][b] = railway[b][a] = true;
        }
        if (railway[0][n - 1]) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    railway[i][j] = !railway[i][j];
                }
            }
        }
        int inf = (int) 1e8;
        int[] dists = new int[n];
        boolean[] visited = new boolean[n];
        Arrays.fill(dists, inf);
        dists[0] = 0;
        while (true) {
            int head = -1;
            for (int j = 0; j < n; j++) {
                if (visited[j]) {
                    continue;
                }
                if (head == -1 || dists[head] > dists[j]) {
                    head = j;
                }
            }
            if (head == -1) {
                break;
            }
            visited[head] = true;
            for (int i = 0; i < n; i++) {
                if (railway[head][i]) {
                    dists[i] = Math.min(dists[i], dists[head] + 1);
                }
            }
        }
        if(dists[n - 1] == inf){
            out.println(-1);
        }else{
            out.println(dists[n - 1]);
        }
    }
}
