package on2021_07.on2021_07_27_Codeforces___Codeforces_Round__685__Div__2_.F__Nullify_The_Matrix;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class FNullifyTheMatrix {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] nim = new int[1000];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                nim[i + j] ^= in.ri();
            }
        }
        int max = Arrays.stream(nim).max().orElse(-1);
        out.println(max == 0 ? "Jeel" : "Ashish");
    }
}
