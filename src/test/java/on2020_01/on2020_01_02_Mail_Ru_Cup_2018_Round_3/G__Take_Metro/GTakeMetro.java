package on2020_01.on2020_01_02_Mail_Ru_Cup_2018_Round_3.G__Take_Metro;



import template.datastructure.PersistentArray;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class GTakeMetro {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        m = in.readInt();
        int s = in.readInt() - 1;
        long t = in.readLong();

        while (t % n != 0) {
            s = next(s, t);
            t--;
        }

        PersistentArray<Integer>[] dp = new PersistentArray[n + 1];
        dp[0] = new PersistentArray<>();
        for (int i = 0; i < n; i++) {
            dp[0] = dp[0].insert(dp[0].size() + 1, i);
        }

        for (int i = 1; i <= n; i++) {
            dp[i] = new PersistentArray<>();
            {
                int l = 0 + i;
                int r = (m - 1 + i) % n;
                if (l <= r) {
                    dp[i] = dp[i].merge(dp[i - 1].interval(l + 1, r + 1));
                } else {
                    dp[i] = dp[i].merge(dp[i - 1].interval(l + 1, n));
                    dp[i] = dp[i].merge(dp[i - 1].interval(1, r + 1));
                }
            }
            {
                int l = DigitUtils.mod(m - i, n);
                int r = DigitUtils.mod(n - 1 - i, n);
                if (l <= r) {
                    dp[i] = dp[i].merge(dp[i - 1].interval(l + 1, r + 1));
                } else {
                    dp[i] = dp[i].merge(dp[i - 1].interval(l + 1, n));
                    dp[i] = dp[i].merge(dp[i - 1].interval(1, r + 1));
                }
            }
        }

        int[] x = new int[n];
        for(int i = 0; i < n; i++){
            x[i] = dp[n].get(i + 1);
        }

        int[] ans = power(x, t / n);
        int entry = ans[s] + 1;
        out.println(entry);
    }

    public int[] merge(int[] a, int[] b){
        int[] ans = new int[n];
        for(int i = 0; i < n; i++){
            ans[i] = a[b[i]];
        }
        return ans;
    }

    public int[] power(int[] x, long exp){
        if(exp == 0){
            int[] ans = new int[n];
            for(int i = 0; i < n; i++){
                ans[i] = i;
            }
            return ans;
        }
        int[] ans = power(x, exp / 2);
        ans = merge(ans, ans);
        if(exp % 2 == 1){
            ans = merge(ans, x);
        }
        return ans;
    }

    int n;
    int m;

    public int next(int i, long t) {
        if (i < m) {
            return DigitUtils.mod(i + t, n);
        } else {
            return DigitUtils.mod(i - t, n);
        }
    }
}
