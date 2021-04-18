package on2021_03.on2021_03_29_Codeforces___Codeforces_Round__225__Div__1_.E__Vowels;



import template.io.FastInput;
import template.io.FastOutput;
import template.polynomial.FastWalshHadamardTransform;

public class EVowels {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] cnt = new int[1 << 24];
        for (int i = 0; i < n; i++) {
            int bit = 0;
            for (char c : in.rs().toCharArray()) {
                bit |= 1 << (c - 'a');
            }
            cnt[bit]++;
        }
        FastWalshHadamardTransform.orFWT(cnt, 0, cnt.length - 1);
        int ans = 0;
        for(int i = 0; i < 1 << 24; i++){
            int cand = (n - cnt[i]) * (n - cnt[i]);
            ans ^= cand;
        }
        out.println(ans);
    }
}
