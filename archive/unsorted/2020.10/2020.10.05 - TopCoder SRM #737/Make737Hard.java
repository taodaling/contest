package contest;

import java.util.Arrays;

public class Make737Hard {
    public String make(int X) {
        if (X == 0) {
            return "7";
        }


        int[] last = new int[X + 1];
        int[] dp = new int[X + 1];
        for (int i = 2; i <= 373; i++) {
            int middle = i / 2;
            if (middle * (i - middle) * (373 - i) < X) {
                continue;
            }
            int[] coes = new int[i / 2];
            for (int j = 1; j <= i / 2; j++) {
                int l = j;
                int r = i - j;
                coes[j - 1] = l * r;
            }
            solve(coes, dp, last);

            if (dp[X] + i > 373) {
                continue;
            }

            //find
            StringBuilder builder = new StringBuilder();
            int[] alloc = new int[i / 2 + 1];
            for (int cur = X; cur != 0; cur -= coes[last[cur]]) {
                alloc[last[cur]]++;
            }
            for (int j = 1; j <= i / 2; j++) {
                builder.append('7');
                for (int k = 0; k < alloc[j - 1]; k++) {
                    builder.append('3');
                }
            }
            for(int j = i / 2 + 1; j <= i; j++){
                builder.append('7');
            }
            return builder.toString();
        }

        throw new RuntimeException();
    }

    int inf = (int) 1e9;

    public void solve(int[] coes, int[] dp, int[] last) {
        Arrays.fill(dp, inf);
        Arrays.fill(last, -1);
        dp[0] = 0;
        for (int i = 1; i < dp.length; i++) {
            for (int j = 0; j < coes.length; j++) {
                if (coes[j] > i) {
                    continue;
                }
                if (dp[i - coes[j]] + 1 < dp[i]) {
                    dp[i] = dp[i - coes[j]] + 1;
                    last[i] = j;
                }
            }
        }
    }
}
