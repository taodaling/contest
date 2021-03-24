package on2021_03.on2021_03_21_AtCoder___AtCoder_Regular_Contest_115.B___Plus_Matrix;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class BPlusMatrix {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[][] c = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                c[i][j] = in.ri();
            }
        }
        int[] da = new int[n];
        int[] db = new int[n];
        for (int i = 1; i < n; i++) {
            db[i] = c[0][i] - c[0][i - 1];
            db[i] += db[i - 1];
        }
        for (int i = 1; i < n; i++) {
            da[i] = c[i][0] - c[i - 1][0];
            da[i] += da[i - 1];
        }
        int a0 = -Arrays.stream(da).min().orElse(-1);
        if (a0 < 0) {
            a0 = 0;
        }
        int b0 = c[0][0] - a0;
        int[] a = new int[n];
        int[] b = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = a0 + da[i];
            b[i] = b0 + db[i];
        }
        for(int i = 0; i < n; i++){
            if(a[i] < 0 || b[i] < 0) {
                out.println("No");
                return;
            }
        }
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(c[i][j] != a[i] + b[j]){
                    out.println("No");
                    return;
                }
            }
        }
        out.println("Yes");
        for(int x : a){
            out.append(x).append(' ');
        }
        for(int x : b){
            out.append(x).append(' ');
        }
    }
}
