package on2020_02.on2020_02_23_Codeforces_Round__454__Div__1__based_on_Technocup_2018_Elimination_Round_4_.D__Power_Tower1;




import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.EulerSieve;
import template.math.GCDs;
import template.math.IntegerModPowerLink;
import template.math.MultiplicativeFunctionSieve;
import template.primitve.generated.datastructure.IntegerHashMap;

public class DPowerTower {

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] w = new int[n];
        for (int i = 0; i < n; i++) {
            w[i] = in.readInt();
        }

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
