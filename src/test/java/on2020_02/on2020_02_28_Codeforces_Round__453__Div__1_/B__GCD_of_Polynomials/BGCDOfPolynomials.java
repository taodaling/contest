package on2020_02.on2020_02_28_Codeforces_Round__453__Div__1_.B__GCD_of_Polynomials;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.polynomial.Polynomials;
import template.primitve.generated.datastructure.IntegerList;

public class BGCDOfPolynomials {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        IntegerList one = new IntegerList();
        IntegerList zero = new IntegerList();
        one.add(1);
        zero.add(1);
        IntegerList[] bot = new IntegerList[]{one, zero};
        for(int i = 0; i < n; i++){
            bot = build(bot[0], bot[1]);
        }

        output(out, bot[0]);
        output(out, bot[1]);
    }

    public void output(FastOutput out, IntegerList p){
        Polynomials.normalize(p);
        out.println(p.size() - 1);
        for(int i = 0; i < p.size(); i++){
            out.append(p.get(i)).append(' ');
        }
        out.println();
    }

    Modular mod = new Modular(2);
    IntegerList x = new IntegerList();

    {
        x.add(0);
        x.add(1);
    }

    public IntegerList[] build(IntegerList a, IntegerList b) {
        IntegerList first = new IntegerList();
        Polynomials.mul(a, x, first, mod);
        IntegerList ans = new IntegerList();
        Polynomials.plus(first, b, ans, mod);
        return new IntegerList[]{ans, a};
    }
}
