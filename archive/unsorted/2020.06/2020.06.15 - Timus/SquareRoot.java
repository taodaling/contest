package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.math.Power;
import template.math.QuadraticResidue;
import template.primitve.generated.datastructure.IntegerList;

public class SquareRoot {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.readInt();
        int n = in.readInt();
        Modular mod = new Modular(n);
        QuadraticResidue qr = new QuadraticResidue(mod);
        int x = qr.square(a);
        if (x == -1) {
            out.println("No root");
            return;
        }
        IntegerList ans = new IntegerList(2);
        ans.add(x);
        ans.add(mod.valueOf(-x));
        ans.unique();
        for(int i = 0; i < ans.size(); i++){
            out.append(ans.get(i)).append(' ');
        }
        out.println();
    }
}
