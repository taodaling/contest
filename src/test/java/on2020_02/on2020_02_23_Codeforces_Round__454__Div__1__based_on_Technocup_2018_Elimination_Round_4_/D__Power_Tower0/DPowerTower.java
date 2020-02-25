package on2020_02.on2020_02_23_Codeforces_Round__454__Div__1__based_on_Technocup_2018_Elimination_Round_4_.D__Power_Tower0;




import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.EulerSieve;
import template.math.Factorization;
import template.math.GCDs;
import template.math.IntegerModPowerLink;
import template.math.MultiplicativeFunctionSieve;
import template.math.PollardRho;
import template.primitve.generated.datastructure.IntegerHashMap;

public class DPowerTower {


    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] w = new int[n];
        for (int i = 0; i < n; i++) {
            w[i] = in.readInt();
        }
//        p = new int[n][4];
//        for (int i = 0; i < n; i++) {
//            p[i][0] = w[i];
//        }
//        for (int j = 1; j < 4; j++) {
//            for (int i = 0; i + j < n; i++) {
//                p[i][j] = pow(w[i], p[i + 1][j - 1], limit);
//            }
//        }

        IntegerModPowerLink link = new IntegerModPowerLink(i -> w[i]);
        int q = in.readInt();
        for (int i = 0; i < q; i++) {
            int l = in.readInt() - 1;
            int r = in.readInt() - 1;
            int ans = link.query(l, r, m);
            out.println(ans);
        }
    }

}
