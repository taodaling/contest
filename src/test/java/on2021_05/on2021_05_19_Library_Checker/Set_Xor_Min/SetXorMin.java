package on2021_05.on2021_05_19_Library_Checker.Set_Xor_Min;



import template.datastructure.BinaryTreeBeta;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerHashMap;
import template.primitve.generated.datastructure.IntegerHashSet;

public class SetXorMin {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int q = in.ri();
        BinaryTreeBeta bt = new BinaryTreeBeta((1 << 30) - 1, q);
        for (int i = 0; i < q; i++) {
            int t = in.ri();
            int x = in.ri();
            if (t == 0) {
                if (bt.intervalSize(x, x) == 0) {
                    bt.add(x, 1);
                }
            } else if (t == 1) {
                if (bt.intervalSize(x, x) == 1) {
                    bt.add(x, -1);
                }
            } else {
                int ans = bt.minXor(x) ^ x;
                out.println(ans);
            }
        }
    }
}
