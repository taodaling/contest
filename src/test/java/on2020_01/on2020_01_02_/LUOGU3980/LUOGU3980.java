package on2020_01.on2020_01_02_.LUOGU3980;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.LinearProgramming;

public class LUOGU3980 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        LinearProgramming.DualLinearProgramming dlp = new LinearProgramming.DualLinearProgramming(n, m, 1e-8);
        for(int i = 1; i <= n; i++){
            dlp.setConstraintConstant(i, in.readInt());
        }
        for(int i = 1; i <= m; i++){
            int l = in.readInt();
            int r = in.readInt();
            int c = in.readInt();
            dlp.setTargetCoefficient(i, c);
            for(int j = l; j <= r; j++){
                dlp.setConstraintCoefficient(j, i, 1);
            }
        }

        dlp.solve();
        long ans = DigitUtils.round(dlp.minSolution());
        out.append(ans);
    }
}
