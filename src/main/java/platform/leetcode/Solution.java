package platform.leetcode;


import java.util.*;

public class Solution {
    public static void main(String[] args) {
        int[] input = new int[]{};
        int[] output = new int[]{2, 3};
        System.out.println(new Solution());
    }

    public int get(int x, int i) {
        return (x >> i) & 1;
    }

    public int minNumberOfSemesters(int n, int[][] dependencies, int k) {
        int[] pre = new int[n];
        int[] post = new int[n];
        for (int[] e : dependencies) {
            int a = e[0] - 1;
            int b = e[1] - 1;
            pre[b] |= 1 << a;
            post[a] |= 1 << b;
        }

        int[] dp = new int[1 << n];
        dp[0] = 0;
        int inf = (int) 1e8;

        SubsetGenerator sg = new SubsetGenerator();
        for (int i = 1; i < 1 << n; i++) {
            boolean valid = true;
            int set = 0;
            dp[i] = inf;
            for (int j = 0; j < n; j++) {
                if (get(i, j) == 0) {
                    continue;
                }
                if ((pre[j] & i) != pre[j]) {
                    valid = false;
                }
                if ((post[j] & i) == 0) {
                    set |= 1 << j;
                }
            }
            if (!valid) {
                continue;
            }
            sg.reset(set);
            while (sg.hasNext()) {
                int next = sg.next();
                if (next != 0 && Integer.bitCount(next) <= k) {
                    dp[i] = Math.min(dp[i - next] + 1, dp[i]);
                }
            }
        }

        return dp[dp.length - 1];
    }
}

class SubsetGenerator {
    private int m;
    private int x;

    public void reset(int m) {
        this.m = m;
        this.x = m + 1;
    }

    public boolean hasNext() {
        return x != 0;
    }

    public int next() {
        return x = (x - 1) & m;
    }
}
