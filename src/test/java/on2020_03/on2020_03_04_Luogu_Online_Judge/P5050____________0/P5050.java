package on2020_03.on2020_03_04_Luogu_Online_Judge.P5050____________0;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.polynomial.NumberTheoryTransform;
import template.primitve.generated.datastructure.IntegerList;
import template.utils.Debug;

public class P5050 {
    public static void main(String[] args){
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < 100000; i++){
            builder.append(i).append(' ');
        }
        System.out.println(builder);
    }

    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        IntegerList p = new IntegerList(n + 1);
        for (int i = 0; i <= n; i++) {
            p.add(in.readInt());
        }
        IntegerList x = new IntegerList(m);
        IntegerList y = new IntegerList(m);
        for (int i = 0; i < m; i++) {
            x.add(in.readInt());
        }

        Modular mod = new Modular(998244353);
        NumberTheoryTransform ntt = new NumberTheoryTransform(mod);
        ntt.multiApply(p, x, y);

        debug.debug("p", p);
        debug.debug("x", x);
        debug.debug("y", y);

        for (int i = 0; i < m; i++) {
            out.println(y.get(i));
        }
    }
}
