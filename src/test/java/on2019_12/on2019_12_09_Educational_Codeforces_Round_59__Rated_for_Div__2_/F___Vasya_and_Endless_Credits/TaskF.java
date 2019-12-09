package on2019_12.on2019_12_09_Educational_Codeforces_Round_59__Rated_for_Div__2_.F___Vasya_and_Endless_Credits;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Offer[] offers = new Offer[n];
        for (int i = 0; i < n; i++) {
            offers[i] = new Offer();
            offers[i].a = in.readInt();
            offers[i].b = in.readInt();
            offers[i].k = in.readInt();
        }
        Arrays.sort(offers, (a, b) -> -Long.compare(a.b, b.b));
        long[][] dp = new long[n + 1][n + 1];
        SequenceUtils.deepFill(dp, (long) -1e18);
        dp[0][0] = 0;
        for (int i = 1; i <= n; i++) {
            Offer offer = offers[i - 1];
            for (int j = 0; j <= n; j++) {
                dp[i][j] = dp[i - 1][j];
                dp[i][j] = Math.max(dp[i][j], dp[i - 1][j] + offer.a -  offer.b * offer.k);
                if (j > 0) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - 1] + offer.a - offer.b * (j - 1));
                }
            }
        }

        long max = 0;
        for (int i = 0; i <= n; i++) {
            max = Math.max(dp[n][i], max);
        }

        out.println(max);
    }
}

class Offer {
    long a;
    long b;
    int k;
}