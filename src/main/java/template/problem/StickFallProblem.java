package template.problem;

import template.utils.Debug;
import template.utils.Pair;
import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 在数轴上有n根直立的木棒，第i根木棒的位置为x[i]，长度为l[i]。现在木棒
 * 向左右其中一侧倒下。问被木棍最大覆盖长度，并计算此时木棍倒下的方向。
 *
 * problem: https://codeforces.com/contest/559/problem/E
 */
public class StickFallProblem {
    static long inf = (long) 2e18;

    /**
     * res.a[i] means the direction of stick i, -1 or 1.
     * res.b is the maximum coverage length.
     * <p>
     * O(n^3)
     *
     * @param x
     * @param l
     * @return
     */
    public static Pair<int[], Long> solve(long[] x, long[] l) {
        int n = x.length;
        Stick[] light = new Stick[n + 1];
        for (int i = 0; i < n; i++) {
            light[i] = new Stick();
            light[i].id = i;
            light[i].x = x[i];
            light[i].l = l[i];
        }
        light[n] = new Stick();
        light[n].x = -inf;
        light[n].l = 0;
        Arrays.sort(light, Comparator.comparingLong(t -> t.x));
        long[][][] dp = new long[2][n + 1][n + 1];
        SequenceUtils.deepFill(dp, -inf);
        dp[0][0][0] = 0;
        for (int i = 1; i <= n; i++) {
            for (int k = 0; k <= i; k++) {
                for (int t = 0; t < 2; t++) {
                    //skip
                    dp[t][i][k] = dp[t][i - 1][k];
                }
            }
            for (int k = 0; k < i; k++) {
                for (int t = 0; t < 2; t++) {
                    long r = light[k].x + t * light[k].l;
                    if (r <= light[i].x) {
                        long cand = Math.min(light[i].x - r, light[i].l) + dp[t][i - 1][k];
                        if (cand > dp[0][i][i]) {
                            dp[0][i][i] = cand;
                        }
                    }
                    if (r <= light[i].x + light[i].l) {
                        long cand = Math.min(light[i].x + light[i].l - r, light[i].l) + dp[t][i - 1][k];
                        if (cand > dp[1][i][i]) {
                            dp[1][i][i] = cand;
                        }
                    }
                }
            }

            long left = light[i].x - light[i].l;
            //cross
            for (int j = 0; j < i - 1; j++) {
                for (int t = 0; t <= j; t++) {
                    for (int d = 0; d < 2; d++) {
                        long r = light[t].x + light[t].l * d;
                        if (r >= light[j + 1].x) {
                            continue;
                        }
                        long rr = light[j + 1].x + light[j + 1].l;
                        if (rr < light[i].x || left >= light[j + 1].x) {
                            continue;
                        }
                        long cand = dp[d][j][t] + light[j + 1].x - Math.max(left, r) +
                                light[j + 1].l;
                        if (cand > dp[1][i][j + 1]) {
                            dp[1][i][j + 1] = cand;
                        }
                    }
                }
            }
        }
        long ans = -1;
        int fi = -1;
        int fj = -1;
        int fk = -1;
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= n; j++) {
                for (int k = 0; k < 2; k++) {
                    if (dp[k][i][j] > ans) {
                        ans = dp[k][i][j];
                        fi = i;
                        fj = j;
                        fk = k;
                    }
                }
            }
        }
        int[] sol = new int[n];
        Arrays.fill(sol, -1);
        while (fi > 0) {
            boolean find = false;
            if (dp[fk][fi][fj] == dp[fk][fi - 1][fj]) {
                //skip
                fi--;
                find = true;
            }
            if (fj == fi && !find) {
                for (int k = 0; k < fi && !find; k++) {
                    for (int t = 0; t < 2 && !find; t++) {
                        long r = light[k].x + t * light[k].l;
                        if (r <= light[fi].x && fk == 0) {
                            long cand = Math.min(light[fi].x - r, light[fi].l) + dp[t][fi - 1][k];
                            if (cand == dp[fk][fi][fj]) {
                                sol[light[fi].id] = -1;
                                fi = fi - 1;
                                fk = t;
                                fj = k;
                                find = true;
                                break;
                            }
                        }
                        if (r <= light[fi].x + light[fi].l && fk == 1) {
                            long cand = Math.min(light[fi].x + light[fi].l - r, light[fi].l) + dp[t][fi - 1][k];
                            if (cand == dp[fk][fi][fj]) {
                                sol[light[fi].id] = 1;
                                fi = fi - 1;
                                fk = t;
                                fj = k;
                                find = true;
                                break;
                            }
                        }
                    }
                }
            }

            if (!find && fk == 1) {
                long left = light[fi].x - light[fi].l;
                //cross
                int j = fj - 1;
                for (int t = 0; t <= j && !find; t++) {
                    for (int d = 0; d < 2 && !find; d++) {
                        long r = light[t].x + light[t].l * d;
                        if (r >= light[j + 1].x) {
                            continue;
                        }
                        long rr = light[j + 1].x + light[j + 1].l;
                        if (rr < light[fi].x || left >= light[j + 1].x) {
                            continue;
                        }
                        long cand = dp[d][j][t] + light[j + 1].x - Math.max(left, r) +
                                light[j + 1].l;
                        if (cand == dp[fk][fi][fj]) {
                            sol[light[fi].id] = -1;
                            sol[light[fj].id] = 1;
                            fk = d;
                            fi = j;
                            fj = t;
                            find = true;
                            break;
                        }
                    }
                }
            }
        }
        return new Pair<>(sol, ans);
    }

    static class Stick {
        long x;
        long l;
        int id;
    }
}

