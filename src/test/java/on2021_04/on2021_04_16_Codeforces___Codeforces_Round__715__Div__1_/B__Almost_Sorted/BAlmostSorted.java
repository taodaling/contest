package on2021_04.on2021_04_16_Codeforces___Codeforces_Round__715__Div__1_.B__Almost_Sorted;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;

public class BAlmostSorted {

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long k = in.rl();
        long[] dp = new long[n + 1];
        long[] ps = new long[n + 1];
        long inf = (long) 1e18 + 10;
        ps[0] = dp[0] = 1;
        for (int i = 1; i <= n; i++) {
            dp[i] = ps[i - 1];
            ps[i] = Math.min(inf, ps[i - 1] + dp[i]);
        }
        int l = 1;
        int r = n;

        IntegerArrayList ans = new IntegerArrayList(n);
        while (l <= r) {
            int m = r - l + 1;
            int remain = -1;
            for (int j = m - 1; j >= 0; j--) {
                if (k <= dp[j]) {
                    remain = j;
                    break;
                }
                k -= dp[j];
            }
            if(remain == -1){
                out.println(-1);
                return;
            }
            int ll = r - remain;
            for (int j = ll; j >= l; j--) {
                ans.add(j);
            }
            l = ll + 1;
        }

        if(k != 1){
            out.println(-1);
            return;
        }
        for(int x : ans.toArray()){
            out.append(x).append(' ');
        }
        out.println();
    }


}
