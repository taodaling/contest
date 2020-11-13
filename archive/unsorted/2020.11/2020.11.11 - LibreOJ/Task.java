package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.ModMatrix;

public class Task {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int p = in.readInt();
        int m = in.readInt();
        int mod = (int)1e9 + 7;
        ModMatrix A = new ModMatrix(n, p);
        ModMatrix B = new ModMatrix(p, m);
        for(int i = 0; i < n; i++){
            for(int j = 0; j < p; j++){
                A.set(i, j, DigitUtils.mod(in.readInt(), mod));
            }
        }
        for(int i = 0; i < p; i++){
            for(int j = 0; j < m; j++){
                B.set(i, j, DigitUtils.mod(in.readInt(), mod));
            }
        }
        ModMatrix ans = ModMatrix.mul(A, B, mod);
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                out.append(ans.get(i, j)).append(' ');
            }
            out.println();
        }

    }
}
