package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.ModPrimeRoot;
import template.math.Modular;
import template.primitve.generated.datastructure.IntegerList;

public class DiscreteRoots {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int p = in.readInt();
        int k = in.readInt();
        int a = in.readInt();

        IntegerList list = new IntegerList();
        ModPrimeRoot mpr = new ModPrimeRoot(new Modular(p));
        mpr.allRoot(a, k, list);

        list.sort();
        out.println(list.size());
        for(int i = 0; i < list.size(); i++){
            out.append(list.get(i)).append(' ');
        }
    }
}
