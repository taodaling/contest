package contest;

import com.sun.org.apache.xpath.internal.operations.Mod;
import template.datastructure.IntList;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.ModPrimeRoot;
import template.math.Modular;

public class NOD51_1014 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int p = in.readInt();
        int a = in.readInt();
        ModPrimeRoot root = new ModPrimeRoot(new Modular(p));
        IntList ans = new IntList();
        root.allRoot(a, 2, ans);
        ans.unique();
        for(int i = 0; i < ans.size(); i++){
            out.append(ans.get(i)).append(' ');
        }
    }
}
