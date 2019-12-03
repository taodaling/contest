package on2019_12.on2019_12_03_Educational_DP_Contest.R___Walk;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.ModMatrix;
import template.math.Modular;

public class TaskR {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long k = in.readLong();
        ModMatrix matrix = new ModMatrix(n, n);
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                matrix.set(i, j, in.readInt());
            }
        }
        Modular mod = new Modular(1e9 + 7);
        ModMatrix trans = ModMatrix.pow(matrix, k, mod);
        int ans = 0;
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                ans = mod.plus(ans, trans.get(i, j));
            }
        }

        out.println(ans);
    }
}
