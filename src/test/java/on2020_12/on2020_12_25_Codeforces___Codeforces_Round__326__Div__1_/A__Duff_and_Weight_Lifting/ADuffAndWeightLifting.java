package on2020_12.on2020_12_25_Codeforces___Codeforces_Round__326__Div__1_.A__Duff_and_Weight_Lifting;



import template.io.FastInput;
import template.io.FastOutput;

public class ADuffAndWeightLifting {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] cnts = new int[(int) 1e6 + 1];
        for (int i = 0; i < n; i++) {
            int w = in.ri();
            cnts[w]++;
        }
        int remain = 0;
        int ans = 0;
        for (int i = 0; i < cnts.length; i++) {
            remain += cnts[i];
            ans += remain & 1;
            remain >>= 1;
        }
        while(remain > 0){
            ans += remain & 1;
            remain >>= 1;
        }
        out.println(ans);
    }
}
