package contest;

import java.util.Map;
import java.util.TreeMap;

import template.DigitUtils;
import template.FastInput;
import template.FastOutput;
import template.NumberTheory;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        boolean valid = true;
        TreeMap<Long, Integer>[] maps = new TreeMap[4];
        for (int i = 0; i < 4; i++) {
            maps[i] = new TreeMap<>();
        }
        for (int i = 0; i < m; i++) {
            int a = in.readInt();
            int b = in.readInt();
            int c = in.readInt();

            Long key = DigitUtils.asLong(a, b);
            maps[Math.min(3, Math.abs(a - b))].put(key, c);
        }
        int[] diagonal = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            diagonal[i] = maps[0].getOrDefault(DigitUtils.asLong(i, i), -1);
        }
        int[] bottom = new int[n + 1];
        int[] top = new int[n + 1];
        for (int i = 2; i <= n; i++) {
            bottom[i] = maps[1].getOrDefault(DigitUtils.asLong(i, i - 1), -1);
            top[i] = maps[1].getOrDefault(DigitUtils.asLong(i - 1, i), -1);
        }
        boolean[] decided = new boolean[n + 1];
        for (int i = 3; i <= n; i++) {
            Integer up = maps[2].get(DigitUtils.asLong(i - 2, i));
            Integer bot = maps[2].get(DigitUtils.asLong(i, i - 2));
            if (up != null && bot != null) {
                if (diagonal[i - 1] == -1) {
                    diagonal[i - 1] = (up + bot) % 2;
                } else if (diagonal[i - 1] != (up + bot) % 2) {
                    valid = false;
                }
            }
            if (up != null || bot != null) {
                decided[i] = true;
            }
        }


        NumberTheory.Modular mod = new NumberTheory.Modular(998244353);
        NumberTheory.Power pow = new NumberTheory.Power(mod);

        int[][] dp = new int[n + 1][2];
        if (diagonal[1] == -1) {
            dp[1][0] = dp[1][1] = 1;
        } else {
            dp[1][diagonal[1]] = 1;
        }

        for (int i = 2; i <= n; i++) {
            if (bottom[i] != -1 && top[i] != -1) {
                dp[i][0] = dp[i - 1][DigitUtils.mod(bottom[i] + top[i], 2)];
                dp[i][1] = dp[i - 1][DigitUtils.mod(bottom[i] + top[i] + 1, 2)];
            } else {
                dp[i][1] = dp[i][0] = mod.plus(dp[i - 1][0], dp[i - 1][1]);
            }
            if (diagonal[i] != -1) {
                dp[i][1 - diagonal[i]] = 0;
            }
        }

        int totalWay = mod.plus(dp[n][0], dp[n][1]);
        for (int i = 2; i <= n; i++) {
            if (top[i] == -1 && bottom[i] == -1) {
                totalWay = mod.mul(totalWay, 2);
            }
        }
        for (int i = 3; i <= n; i++) {
            if (!decided[i]) {
                totalWay = mod.mul(totalWay, 2);
            }
        }

        long remain = ((long) n * n - n - 2 * (n - 1) - 2 * (n - 2)) / 2;
        while (!maps[3].isEmpty()) {
            Map.Entry<Long, Integer> kv = maps[3].pollFirstEntry();
            Long otherSide = DigitUtils.asLong(DigitUtils.lowBit(kv.getKey()), DigitUtils.highBit(kv.getKey()));
            if (maps[3].containsKey(otherSide)) {
                Integer otherSideValue = maps[3].remove(otherSide);
                if (!kv.getValue().equals(otherSideValue)) {
                    valid = false;
                }
            }
            remain--;
        }

        totalWay = mod.mul(totalWay, pow.pow(2, remain));

        if (valid) {
            out.println(totalWay);
        } else {
            out.println(0);
        }
    }
}
