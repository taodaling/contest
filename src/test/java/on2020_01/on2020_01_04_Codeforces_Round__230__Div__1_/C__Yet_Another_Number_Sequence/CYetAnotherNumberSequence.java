package on2020_01.on2020_01_04_Codeforces_Round__230__Div__1_.C__Yet_Another_Number_Sequence;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.LinearRecurrenceSolver;
import template.math.Modular;
import template.math.Power;
import template.primitve.generated.IntegerList;

public class CYetAnotherNumberSequence {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.readLong();
        int k = in.readInt();

        Modular mod = new Modular(1000000007);
        Power power = new Power(mod);

        int items = 200;
        int[] fib = new int[items];
        fib[0] = 1;
        fib[1] = 2;
        int[] a = new int[items];
        for (int i = 2; i < items; i++) {
            fib[i] = mod.plus(fib[i - 1], fib[i - 2]);
        }
        for (int i = 0; i < items; i++) {
            a[i] = mod.mul(fib[i], power.pow(i + 1, k));
            if (i > 0) {
                a[i] = mod.plus(a[i], a[i - 1]);
            }
        }

        IntegerList seq = new IntegerList();
        seq.addAll(a);
        LinearRecurrenceSolver solver = LinearRecurrenceSolver.newSolverFromSequence(seq, mod);
        int ans = solver.solve(n - 1, seq);

        out.println(ans);
    }
}
