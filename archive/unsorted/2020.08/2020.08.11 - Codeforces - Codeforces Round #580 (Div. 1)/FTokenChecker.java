package contest;

import chelper.AbstractChecker;
import net.egork.chelper.tester.Verdict;
import net.egork.chelper.checkers.Checker;
import template.datastructure.PermutationNode;
import template.io.FastInput;
import template.math.Permutation;

public class FTokenChecker extends AbstractChecker {
    public FTokenChecker(String parameters) {
        super(parameters);
    }

    public int count(PermutationNode root) {
        int ans = 1;
        if (root.adj.size() > 0 && root.join) {
            int n = root.adj.size();
            ans += n * (n - 1) / 2 - 1;
        }
        for (PermutationNode node : root.adj) {
            ans += count(node);
        }
        return ans;
    }

    @Override
    public Verdict check(FastInput stdin, FastInput expect, FastInput actual) {
        int q = stdin.readInt();
        for (int i = 0; i < q; i++) {
            int n = stdin.readInt();
            int k = stdin.readInt();
            if (actual.readString().equals("NO")) {
                continue;
            }
            int[] perm = new int[n];
            actual.populate(perm);
            for (int j = 0; j < n; j++) {
                perm[j]--;
            }

            PermutationNode root = PermutationNode.build(perm);
            int count = count(root);
            if (count != k) {
                return Verdict.WA;
            }
        }

        return Verdict.OK;
    }
}
