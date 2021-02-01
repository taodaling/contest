package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.string.AhoCorasick;

import java.util.Arrays;

public class Problem1VideoGame {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        AhoCorasick ac = new AhoCorasick('A', 'C', n * 20);
        int[] end = new int[n];
        for (int i = 0; i < n; i++) {
            char[] s = in.rs().toCharArray();
            ac.prepareBuild();
            for (char c : s) {
                ac.build(c);
            }
            end[i] = ac.buildLast;
        }
        int[] topo = ac.endBuild();
        int m = topo.length;
        int[] score = new int[m];
        for (int x : end) {
            score[x]++;
        }
        for (int i = 1; i < m; i++) {
            int node = topo[i];
            int p = ac.fails[node];
            score[node] += score[p];
        }
        long inf = (long) 1e18;
        long[] prev = new long[m];
        long[] next = new long[m];
        Arrays.fill(prev, -inf);
        prev[0] = 0;
        for (int i = 0; i < k; i++) {
            Arrays.fill(next, -inf);
            for (int j = 0; j < m; j++) {
                for (int c = 0; c < 3; c++) {
                    int to = ac.next[c][j];
                    next[to] = Math.max(next[to], prev[j] + score[to]);
                }
            }
            long[] tmp = prev;
            prev = next;
            next = tmp;
        }

        long ans = Arrays.stream(prev).max().orElse(-1);
        out.println(ans);
    }
}
