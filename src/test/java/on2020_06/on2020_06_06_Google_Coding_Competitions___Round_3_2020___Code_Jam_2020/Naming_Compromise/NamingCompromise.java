package on2020_06.on2020_06_06_Google_Coding_Competitions___Round_3_2020___Code_Jam_2020.Naming_Compromise;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerList;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class NamingCompromise {
 //   Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        if (!in.hasMore()) {
            throw new UnknownError();
        }
        out.printf("Case #%d: ", testNumber);
        String a = in.readString();
        String b = in.readString();
        int dist = editDistance(a, b);
        StringBuilder ans = new StringBuilder(a.length() + b.length());
        backtrace(a.length() - 1, b.length() - 1, ans, dist / 2);
        ans.reverse();
        out.println(ans);

//        debug.debug("dist", dist);
//        debug.debug("ans->a", editDistance(a, ans.toString()));
//        debug.debug("ans->b", editDistance(b, ans.toString()));
    }

    char[] a;
    char[] b;
    int[][] dp;

    public void backtrace(int i, int j, StringBuilder builder, int more) {
        if (more == 0) {
            for (int k = i; k >= 0; k--) {
                builder.append(a[k]);
            }
            return;
        }
        if (dp(i, j) == dp(i, j - 1) + 1) {
            builder.append(b[j]);
            backtrace(i, j - 1, builder, more - 1);
            return;
        }
        if (dp(i, j) == dp(i - 1, j) + 1) {
            //builder.append(a[i]);
            backtrace(i - 1, j, builder, more - 1);
            return;
        }
        if (dp(i, j) == dp(i - 1, j - 1) + 1) {
            builder.append(b[j]);
            backtrace(i - 1, j - 1, builder, more - 1);
            return;
        }
        builder.append(a[i]);
        backtrace(i - 1, j - 1, builder, more);
    }

    public int editDistance(String a, String b) {
        this.a = a.toCharArray();
        this.b = b.toCharArray();
        dp = new int[this.a.length][this.b.length];
        SequenceUtils.deepFill(dp, -1);
        return dp(a.length() - 1, b.length() - 1);
    }


    public int dp(int i, int j) {
        if (i < 0 || j < 0) {
            return Math.max(i, j) + 1;
        }
        if (dp[i][j] == -1) {
            dp[i][j] = Math.min(dp(i, j - 1) + 1, dp(i - 1, j) + 1);
            dp[i][j] = Math.min(dp[i][j], dp(i - 1, j - 1) + 1);
            if (a[i] == b[j]) {
                dp[i][j] = Math.min(dp[i][j], dp(i - 1, j - 1));
            }
        }
        return dp[i][j];
    }
}
