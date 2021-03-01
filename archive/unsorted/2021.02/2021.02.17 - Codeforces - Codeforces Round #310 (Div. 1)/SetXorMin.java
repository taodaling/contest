package contest;

import template.datastructure.BinaryTreeBeta;
import template.io.FastInput;
import template.io.FastOutput;

public class SetXorMin {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int q = in.ri();
        BinaryTreeBeta bt = new BinaryTreeBeta(3, q);
        for (int i = 0; i < q; i++) {
            int t = in.ri();
            int x = in.ri();
            if (t == 0) {
                bt.add(x, 1);
            } else if (t == 1) {
                int cnt = bt.prefixSize(x);
                if (x > 0) {
                    cnt -= bt.prefixSize(x - 1);
                }
                if (cnt > 0) {
                    bt.add(x, -1);
                }
            } else {
                int e = bt.minXor(x);
                out.println(e ^ x);
            }
        }
    }
}
