package on2021_03.on2021_03_21_AtCoder___AtCoder_Regular_Contest_115.A___Two_Choices;



import template.io.FastInput;
import template.io.FastOutput;
import template.polynomial.FastWalshHadamardTransform;

public class ATwoChoices {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        long[] cnt = new long[1 << m];
        for(int i = 0; i < n; i++){
            int v = 0;
            for(int j = 0; j < m; j++){
                v |= (in.rc() - '0') << j;
            }
            cnt[v]++;
        }
        FastWalshHadamardTransform.xorFWT(cnt, 0, cnt.length - 1);
        FastWalshHadamardTransform.dotMul(cnt, cnt, cnt, 0, cnt.length - 1);
        FastWalshHadamardTransform.xorIFWT(cnt, 0, cnt.length - 1);

        long ans = 0;
        for(int i = 0; i < 1 << m; i++){
            if(Integer.bitCount(i) % 2 == 1){
                ans += cnt[i];
            }
        }
        ans /= 2;
        out.println(ans);
    }
}
