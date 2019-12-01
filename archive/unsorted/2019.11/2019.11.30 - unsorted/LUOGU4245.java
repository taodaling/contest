package contest;

import numeric.FFT;
import template.io.FastInput;
import template.io.FastOutput;

public class LUOGU4245 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int p = in.readInt();

        int[] a = new int[n + 1];
        for(int i = 0; i <= n; i++){
            a[i] = in.readInt();
        }
        int[] b = new int[m + 1];
        for(int i = 0; i <= m; i++){
            b[i] = in.readInt();
        }
        int[] res = FFT.multiplyMod(a, b, p);
        for(int i = 0; i < res.length; i++){
            out.append(res[i]).append(' ');
        }
    }
}
