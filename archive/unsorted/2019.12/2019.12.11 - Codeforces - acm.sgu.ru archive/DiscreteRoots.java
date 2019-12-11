package contest;

import template.datastructure.IntList;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.ModPrimeRoot;
import template.math.Modular;

public class DiscreteRoots {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int p = in.readInt();
        int k = in.readInt();
        int a = in.readInt();
        ModPrimeRoot root = new ModPrimeRoot(new Modular(p));
        IntList ans = new IntList();
        root.allRoot(a, k, ans);
        ans.unique();
        out.println(ans.size());
        for (int i = 0; i < ans.size(); i++) {
            out.println(ans.get(i));
        }
    }
}
