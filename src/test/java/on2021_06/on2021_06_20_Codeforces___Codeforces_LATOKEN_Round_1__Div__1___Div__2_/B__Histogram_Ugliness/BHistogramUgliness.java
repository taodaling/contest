package on2021_06.on2021_06_20_Codeforces___Codeforces_LATOKEN_Round_1__Div__1___Div__2_.B__Histogram_Ugliness;



import template.io.FastInput;
import template.io.FastOutput;

public class BHistogramUgliness {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        long ans = 0;
        int last = 0;
        for (int i = 0; i < n; i++) {
            ans += Math.abs(last - a[i]);
            last = a[i];
        }
        ans += last;

        for(int i = 0; i < n; i++){
            int l = i == 0 ? 0 : a[i - 1];
            int r = i == n - 1 ? 0 : a[i + 1];
            if(a[i] > l && a[i] > r){
                ans -= a[i] - Math.max(l, r);
            }
        }

        out.println(ans);
    }
}
