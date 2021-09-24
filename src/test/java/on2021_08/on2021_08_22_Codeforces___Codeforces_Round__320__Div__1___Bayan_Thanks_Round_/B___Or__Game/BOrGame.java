package on2021_08.on2021_08_22_Codeforces___Codeforces_Round__320__Div__1___Bayan_Thanks_Round_.B___Or__Game;



import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;

public class BOrGame {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int x = in.ri();
        long xk = 1;
        for(int i = 0; i < k; i++){
            xk *= x;
        }
        long[] a = in.rl(n);
        long[] prefix = new long[n];
        long[] suffix = new long[n];
        for(int i = 0; i < n; i++){
            prefix[i] = a[i];
            if(i > 0){
                prefix[i] |= prefix[i - 1];
            }
        }
        for(int i = n - 1; i >= 0; i--){
            suffix[i] = a[i];
            if(i + 1 < n){
                suffix[i] |= suffix[i + 1];
            }
        }

        long ans = 0;
        for(int i = 0; i < n; i++){
            long or = 0;
            if(i > 0){
                or |= prefix[i - 1];
            }
            if(i + 1 < n){
                or |= suffix[i + 1];
            }
            or |= xk * a[i];
            ans = Math.max(ans, or);
        }
        out.println(ans);
    }
}
