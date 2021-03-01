package contest;

import template.datastructure.BinaryTreeBeta;
import template.io.FastInput;
import template.io.FastOutput;

public class MaximumXorSubarray {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int ans = 0;
        int n = in.readInt();
        int height = 29;
        BinaryTreeBeta bt = new BinaryTreeBeta(height, n + 1);
        bt.add(0, 1);
        int ps = 0;
        for (int i = 0; i < n; i++) {
            ps ^= in.readInt();
            ans = Math.max(ans, bt.maxXor(ps) ^ ps);
            bt.add(ps, 1);
        }
        out.println(ans);
    }
}
