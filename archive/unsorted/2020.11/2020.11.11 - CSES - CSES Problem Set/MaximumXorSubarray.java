package contest;

import template.binary.Log2;
import template.datastructure.BinaryTree;
import template.io.FastInput;
import template.io.FastOutput;

public class MaximumXorSubarray {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int ans = 0;
        int n = in.readInt();
        int height = Log2.floorLog((int) 1e9);
        BinaryTree bt = new BinaryTree();
        bt.add(0, height, 1);
        int ps = 0;
        for (int i = 0; i < n; i++) {
            ps ^= in.readInt();
            ans = Math.max(ans, bt.maxXor(ps, height));
            bt.add(ps, height, 1);
        }
        out.println(ans);
    }
}
