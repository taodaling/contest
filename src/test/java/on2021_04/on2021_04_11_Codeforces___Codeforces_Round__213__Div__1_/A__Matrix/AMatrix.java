package on2021_04.on2021_04_11_Codeforces___Codeforces_Round__213__Div__1_.A__Matrix;



import template.io.FastInput;
import template.io.FastOutput;

public class AMatrix {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.ri();
        char[] s = in.rs().toCharArray();
        for (int i = 0; i < s.length; i++) {
            s[i] -= '0';
        }
        int n = s.length;
        int lim = 50000;
        int[] cnt = new int[lim];
        for (int i = 0; i < n; i++) {
            int sum = 0;
            for (int j = i; j < n; j++) {
                sum += s[j];
                cnt[sum]++;
            }
        }
        long ans = 0;
        for (int i = 1; i < lim; i++) {
            if (a % i == 0 && a / i < lim) {
                ans += (long) cnt[i] * cnt[a / i];
            }
        }
        if (a == 0) {
            for(int i = 0; i < lim; i++){
                ans += (long)cnt[0] * cnt[i];
            }
        }

        out.println(ans);
    }
}
