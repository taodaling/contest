package contest;

import template.datastructure.BinaryTreeBeta;
import template.io.FastInput;
import template.io.FastOutput;

public class BMishaAndPermutationsSummation {
    BinaryTreeBeta tree = new BinaryTreeBeta(17, 2 * 200000);

    int[] getOrder(int[] p) {
        tree.clear();
        int n = p.length;
        int[] order = new int[n];
        for (int i = n - 1; i >= 0; i--) {
            order[i] = tree.prefixSize(p[i]);
            tree.add(p[i], 1);
        }
        return order;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] p = getOrder(in.ri(n));
        int[] q = getOrder(in.ri(n));
        tree.clear();
        for (int i = 0; i < n; i++) {
            tree.add(i, 1);
        }
        int[] ans = new int[n];
        int carry = 0;
        for (int i = n - 1; i >= 0; i--) {
            ans[i] = p[i] + q[i] + carry;
            carry = ans[i] / (n - i);
            ans[i] %= (n - i);
        }
        for (int i = 0; i < n; i++) {
            int kth = tree.kthSmallest(ans[i] + 1);
            out.append(kth).append(' ');
            tree.add(kth, -1);
        }
    }
}
